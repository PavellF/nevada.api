package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfilePreferencesController {

	private TokenContext securityContext;
	private ProfilePreferencesService profilePrefsService;
	
	@Autowired
	public ProfilePreferencesController(TokenContext securityContext,
			ProfilePreferencesService profilePrefsService) {
		this.securityContext = securityContext;
		this.profilePrefsService = profilePrefsService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".profilePreferences+json", 
			APPLICATION_ACCEPT_PREFIX+".profilePreferences+xml"},
			path = "/profiles/{owner_id}/profilePreferences")	
	@Secured(access = Access.READ, scope = { Scope.PERSON_INFO })
	public ResponseEntity<ProfilePreferencesDTO> getProfilePreferences(
			@PathVariable("owner_id") int ownerId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) {
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		final boolean isSusperToken = securityContext.getToken().isSuper();
		final boolean isOwnedByRequesting = issuer.getIdAsInt() == ownerId;
		
		
		if (isOwnedByRequesting || isSusperToken) {
			return ResponseEntity.ok(profilePrefsService.getForProfile(
					ownerId, version));
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@PutMapping(consumes = { 
			APPLICATION_ACCEPT_PREFIX+".profilePreferences+json", 
			APPLICATION_ACCEPT_PREFIX+".profilePreferences+xml"},
			path = "/profilePreferences")
	@Secured(access = Access.READ_WRITE, scope = { Scope.PERSON_INFO })
	public ResponseEntity<ProfilePreferencesDTO> updateProfilePrefs(
			HttpEntity<ProfilePreferencesDTO> entity,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final ProfilePreferencesDTO posted = entity.getBody();
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		final boolean isSusperToken = securityContext.getToken().isSuper();
		final boolean isOwnedByRequesting = 
				issuer.getIdAsInt() == posted.getProfileId();
		
		if (isOwnedByRequesting || isSusperToken) {
			profilePrefsService.update(posted, version);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	
}
