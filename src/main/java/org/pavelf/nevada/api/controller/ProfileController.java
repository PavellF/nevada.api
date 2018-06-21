package org.pavelf.nevada.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static  org.springframework.http.MediaType.*;

import java.net.URI;
import java.security.Principal;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.Null;
import javax.websocket.server.PathParam;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO.Builder;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.GuestService;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PeopleService;
import org.pavelf.nevada.api.service.PhotoService;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@RestController()
@RequestMapping("/profiles")
public class ProfileController {

	private TokenContext principal;
	private ProfileService profileService;
	private PhotoService photoService;
	private MessageService messageService;
	private GuestService guestService;
	
	@Autowired
	public ProfileController(TokenContext principal,
			ProfileService profileService, PhotoService photoService,
			MessageService messageService, GuestService guestService) {
		this.principal = principal;
		this.profileService = profileService;
		this.photoService = photoService;
		this.messageService = messageService;
		this.guestService = guestService;
	}

	/*
	 * Produces xml or json representation of the user profile.
	 * Expected headers: 
	 * Accept-Language(optional), Accept-Charset(optional), Authorization(optional),
	 * Accept(mandatory, with versioning information: application/xml;version=1.0)
	 * @param id profile id.
	 * */
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"},
			path = "/{id}")	
	public ResponseEntity<ProfileDTO> getProfile(@PathVariable("id") int id, 
			HttpEntity<ProfileDTO> entity, 
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		if(!principal.isAuthorized()) {
			return ResponseEntity.ok(profileService.read(id, true, version));
		} 
		
		User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		
		if (issuer.getIdAsInt() == id) {
			return ResponseEntity.ok(profileService.read(id, false, version));
		} else {
			return ResponseEntity.ok(profileService.read(id, true, version));
		}
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"})
	@Secured(access = Access.READ_WRITE, scope = { Scope.ACCOUNT })
	public ResponseEntity<ProfileDTO> postProfile(
			HttpEntity<ProfileDTO> entity,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final ProfileDTO posted = entity.getBody();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		Integer id = profileService.create(posted, version);
		return ResponseEntity.created(URI.create("/profiles/" + id)).build();
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"},
			path = "/{ids}-**")	
	public ResponseEntity<Set<ProfileDTO>> getProfiles(@PathVariable("ids") String ids, 
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		
		Set<Integer> profileIds = Pattern.compile("-")
				.splitAsStream(ids)
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
		
		if (profileIds.size() > 100) {
			throw new WebApplicationException(OUT_OF_BOUND_VALUE);
		}
		
		return ResponseEntity.ok(profileService.readAll(profileIds, version));
	}
	
	/**
	 * @return 204 "No Content"
	 * */
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"})
	@Secured(access = Access.READ_WRITE, 
	scope = { Scope.ACCOUNT, Scope.PERSON_INFO })
	public ResponseEntity<ProfileDTO> updateProfile(HttpEntity<ProfileDTO> entity, 
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		final ProfileDTO toUpdate = entity.getBody();
		
		if (toUpdate == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (toUpdate.getId() == null) {
			throw new WebApplicationException(REQUIRED_BODY_PROPERTY);
		}
		//can change anything but rating
		if (principal.getToken().isSuper()) {
			profileService.update(entity.getBody(), version);
			return ResponseEntity.noContent().build();
		} 
		
		if (toUpdate.getPopularity() != null 
				|| issuer.getIdAsInt() != toUpdate.getId()) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		//can change anything but rating, popularity
		if (principal.getToken().hasAccess(2, Scope.ACCOUNT)) {
			if (toUpdate.getPassword() != null) {
				final char[] oldPassword = toUpdate.getOldPassword();
				if (oldPassword == null) {
					throw new WebApplicationException(NO_PREVIOUS_PASSWORD);
				}
				if(!profileService.arePasswordsEqual(
						oldPassword, toUpdate.getId())) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			}
			
			profileService.update(entity.getBody(), version);
			return ResponseEntity.noContent().build();
		}
		
		if (toUpdate.getPassword() != null 
				|| toUpdate.getEmail() != null 
				|| toUpdate.getUsername() != null) {
			throw new WebApplicationException(ACCESS_DENIED);
		} 
		
		if (toUpdate.getPictureId() != null) {
			if (!photoService.isBelongsTo(toUpdate.getId(), 
					toUpdate.getPictureId())) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		if (toUpdate.getAboutId() != null) {
			if (!messageService.isBelongsTo(toUpdate.getId(), 
					toUpdate.getAboutId())) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		profileService.update(entity.getBody(), version);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
