package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Owner;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.ApplicationService;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

	private TokenService tokenService;
	private ApplicationService applicationService;
	private ProfileService profileService;
	private TokenContext principal;
	
	@Autowired
	public TokenController(TokenService tokenService,
			ApplicationService applicationService,
			ProfileService profileService, TokenContext principal) {
		this.tokenService = tokenService;
		this.applicationService = applicationService;
		this.profileService = profileService;
		this.principal = principal;
	}

	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"},
			path="/tokens")
	@Secured(access=Access.READ_WRITE, scope = Scope.ACCOUNT)
	public ResponseEntity<TokenDTO> createToken(HttpEntity<TokenDTO> entity) {
		final TokenDTO posted = entity.getBody();
		final Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		
		final Integer appId = posted.getApplicationId();
		final Integer profileId = posted.getProfileId();
		
		if (appId == null) {
			throw new WebApplicationException(UNRECOGNIZED_APPLICATION);
		}
		
		if (applicationService.isSuspended(appId)) {
			throw new WebApplicationException(BANNED_APPLICATION ); 
		}
		
		if (profileId == null) {
			throw new WebApplicationException(UNRECOGNIZED_USER);
		}
		
		if (profileService.isSuspended(profileId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		Integer id = tokenService.create(posted, version);
		return ResponseEntity.created(URI.create("/tokens/" + id)).build();
	}
	

	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"},
			path="/tokens")
	@Secured(access=Access.READ_WRITE, scope = Scope.ACCOUNT)
	public ResponseEntity<TokenDTO> updateToken(HttpEntity<TokenDTO> entity) {
		final TokenDTO posted = entity.getBody();
		final Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		
		if (tokenService.update(posted, version)) {
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(FAILED_UPDATE);
	}
	
	@GetMapping(produces = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"},
			path = "/{owner}/{owner_id}/tokens")	
	@Secured(access=Access.READ, scope = Scope.ACCOUNT)
	public ResponseEntity<List<TokenDTO>> getTokens(@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int id, 
			@RequestHeader HttpHeaders headers) {
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		
		if (owner == Owner.PROFILE) {
			
			if (!principal.getToken().isSuper()) {
				User issuer = principal.getToken().getUser().orElseThrow(() -> 
					new WebApplicationException(UNRECOGNIZED_USER));
				
				if (id != issuer.getIdAsInt()) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			} 
			
			return ResponseEntity.ok(tokenService.getAllForProfile(id, version));
		} else if (owner == Owner.APPLICATION) {
			
			if (!principal.getToken().isSuper()) {
				User issuer = principal.getToken().getUser().orElseThrow(() -> 
					new WebApplicationException(UNRECOGNIZED_USER));
				
				if (!applicationService.isBelongsTo(issuer.getIdAsInt(), id)) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			} 
			
			return ResponseEntity.ok(tokenService.getAllForApplication(id, version));
		}
		
		return ResponseEntity.notFound().build();
	}

}
