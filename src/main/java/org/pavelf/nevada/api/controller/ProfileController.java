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
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PeopleService;
import org.pavelf.nevada.api.service.PhotoService;
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
	
	@Autowired
	public ProfileController(TokenContext principal,
			ProfileService profileService, PhotoService photoService,
			MessageService messageService) {
		this.principal = principal;
		this.profileService = profileService;
		this.photoService = photoService;
		this.messageService = messageService;
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
			@RequestHeader HttpHeaders headers) { 
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		
		if(!principal.isAuthorized()) {
			return ResponseEntity.ok(profileService.read(id, true, version));
		} 
		
		User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		
		if (issuer.getId().equals(String.valueOf(id))) {
			return ResponseEntity.ok(profileService.read(id, false, version));
		} else {
			return ResponseEntity.ok(profileService.read(id, true, version));
		}
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"})
	@Secured(access = Access.READ_WRITE, scope = { Scope.ACCOUNT })
	public ResponseEntity<ProfileDTO> postProfile(HttpEntity<ProfileDTO> entity) {
		final ProfileDTO posted = entity.getBody();
		final Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		Integer id = profileService.create(posted, version);
		return ResponseEntity.created(URI.create("/profiles/" + id)).build();
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"},
			path = "/{ids}")	
	
	public ResponseEntity<Set<ProfileDTO>> getProfiles(@PathVariable("ids") String ids, 
			@RequestHeader HttpHeaders headers) { 
		
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		
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
	scope = { Scope.ACCOUNT, Scope.MESSAGE, Scope.PHOTO })
	public ResponseEntity<ProfileDTO> updateProfile(HttpEntity<ProfileDTO> entity, 
			@RequestHeader HttpHeaders headers) {
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		
		String id = null;
		final ProfileDTO toUpdate = entity.getBody();
		
		if (toUpdate == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (toUpdate.getId() == null) {
			throw new WebApplicationException(REQUIRED_BODY_PROPERTY);
		}
		
		id = toUpdate.getId().toString();
		
		boolean superUserAllowedOnly = true;
		if (principal.getToken().isSuper()) {
			
			if (toUpdate.getPassword() != null) {
				final char[] oldPassword = toUpdate.getOldPassword();
				if (oldPassword == null) {
					throw new WebApplicationException(NO_PREVIOUS_PASSWORD);
				}
				if(!profileService.arePasswordsEqual(oldPassword, toUpdate.getId())) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			}
			
		} else if (principal.getToken().hasAccess(2, Scope.ACCOUNT)) {
			
			if (superUserAllowedOnly = (!id.equals(issuer.getId()) || 
					toUpdate.getPopularity() != null || 
					toUpdate.getRating() != null)) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
			
		} else {
			
			if (superUserAllowedOnly
				|| toUpdate.getPassword() != null || toUpdate.getEmail() != null 
				|| toUpdate.getUsername() != null) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		if (toUpdate.getPictureId() != null) {
			if (!photoService.isBelongsTo(toUpdate.getId(), toUpdate.getPictureId())) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		if (toUpdate.getAboutId() != null) {
			if (!messageService.isBelongsTo(toUpdate.getId(), toUpdate.getAboutId())) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		if (!profileService.update(entity.getBody(), version)) {
			throw new WebApplicationException(FAILED_UPDATE);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
}
