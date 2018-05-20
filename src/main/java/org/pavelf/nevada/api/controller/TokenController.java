package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.net.URI;
import java.time.Instant;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.ApplicationService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("\tokens")
public class TokenController {

	private TokenService tokenService;
	private ApplicationService applicationService;
	
	@Autowired
	public TokenController(TokenService tokenService,
			ApplicationService applicationService) {
		this.tokenService = tokenService;
		this.applicationService = applicationService;
	}

	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"})
	@Secured(access=Access.READ_WRITE, scope = Scope.ACCOUNT)
	public ResponseEntity<TokenDTO> createToken(HttpEntity<TokenDTO> entity) {
		final TokenDTO posted = entity.getBody();
		final Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		final Instant now = Instant.now();
		
		if (posted.getValidUntil().isBefore(now)) {
			throw new WebApplicationException(INVALID_BODY_PROPERTY);
		}
		
		final Integer appId = posted.getApplicationId();
		if (appId == null) {
			throw new WebApplicationException(UNRECOGNIZED_APPLICATION);
		}
		
		ApplicationDTO application = applicationService.getExisting(appId, version);
		if (application.getSuspendedUntil().isAfter(now)) {
			throw new WebApplicationException(BANNED_APPLICATION );
		}
		
		final Integer profileId = posted.getProfileId();
		if (profileId == null) {
			throw new WebApplicationException(UNRECOGNIZED_USER);
		}
		//for banned user..
		
		Integer id = tokenService.create(posted, version);
		return ResponseEntity.created(URI.create("/tokens/" + id)).build();
	}
	
	@GetMapping(produces = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"},
			path = "/{id}")	
	@Secured(access=Access.READ, scope = Scope.ACCOUNT)
	public ResponseEntity<TokenDTO> getToken(@PathVariable("id") int id, 
			@RequestHeader HttpHeaders headers) {
		final Version version = new VersionImpl(headers.getAccept().get(0).getParameter("version"));
		TokenDTO token = tokenService.get(id, version);
		return ResponseEntity.ok(token);
	}
	
	@DeleteMapping(path = "/{id}")
	@Secured(access=Access.READ_WRITE, scope = Scope.ACCOUNT)
	public ResponseEntity<Void> deleteToken(@PathVariable("id") int id) {
		tokenService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
