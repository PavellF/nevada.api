package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.Collection;

/**
 * Exposes some endpoints that can accept and emit {@code Application} entity.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class ApplicationController {

	private ApplicationService applicationService;
	private TokenContext principal;
	
	@Autowired
	public ApplicationController(ApplicationService applicationService,
			TokenContext principal) {
		this.applicationService = applicationService;
		this.principal = principal;
	}

	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".application+json", 
			APPLICATION_ACCEPT_PREFIX+".application+xml"},
			path = "/applications")
	
	@Secured(access=Access.READ_WRITE, scope = Scope.APPLICATION)
	public ResponseEntity<ApplicationDTO> createApplication(
			HttpEntity<ApplicationDTO> entity, 
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final ApplicationDTO posted = entity.getBody();
		final User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		final Integer profileId = posted.getProfileId();
		if (profileId == null) {
			throw new WebApplicationException(UNRECOGNIZED_USER);
		}
		
		if (!principal.getToken().isSuper()) {
				if (profileId != Integer.valueOf(issuer.getId())) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
		}
		
		applicationService.create(posted, version);
		
		return ResponseEntity.created(URI
				.create("profiles/"+profileId+"/applications")).build();
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".application+json", 
			APPLICATION_ACCEPT_PREFIX+".application+xml"},
			path = "/profiles/{owner_id}/applications/")	
	@Secured(access=Access.READ, scope = Scope.APPLICATION)
	public ResponseEntity<Collection<ApplicationDTO>> 
		getApplicationsOwnedByProfile(
			@PathVariable("owner_id") int id, 
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		return ResponseEntity.ok(applicationService
				.getAllForProfile(id, version));
	}
	
	
	@PutMapping(consumes = {APPLICATION_ACCEPT_PREFIX+".application+json", 
			APPLICATION_ACCEPT_PREFIX+".application+xml"}, 
	path="/applications")
	@Secured(access=Access.READ_WRITE, scope = Scope.APPLICATION)
	public ResponseEntity<ApplicationDTO> updateApplication(
			HttpEntity<ApplicationDTO> entity,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final ApplicationDTO toUpdate = entity.getBody();
		final User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		
		if (toUpdate == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (toUpdate.getId() == null) {
			throw new WebApplicationException(REQUIRED_BODY_PROPERTY);
		}
		
		if (!principal.getToken().isSuper()) {
			Integer issuerId = Integer.valueOf(issuer.getId());
			
			if (!applicationService.isBelongsTo(issuerId, toUpdate.getId())
					|| toUpdate.getProfileId() != issuerId) { 
				throw new WebApplicationException(ACCESS_DENIED);
			}
 		}
		
		if (applicationService.update(entity.getBody(), version)) {
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(FAILED_UPDATE);
	}
	
	
}
