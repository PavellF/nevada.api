package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

	private TokenContext securityContext;
	private PeopleService peopleService;
	
	@Autowired
	public PersonController(TokenContext securityContext,
			PeopleService peopleService) {
		this.securityContext = securityContext;
		this.peopleService = peopleService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".person+json", 
			APPLICATION_ACCEPT_PREFIX+".person+xml"},
			path = "/profiles/{owner_id}/persons")	
	public ResponseEntity<PersonDTO> getPerson(
			@PathVariable("owner_id") int ownerId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) {
		
		return ResponseEntity.ok(peopleService.getForProfile(
					ownerId, version));
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".person+json", 
			APPLICATION_ACCEPT_PREFIX+".person+xml"},
			path = "/persons")
	@Secured(access = Access.READ_WRITE, scope = { Scope.PERSON_INFO })
	public ResponseEntity<PersonDTO> postPerson(
			HttpEntity<PersonDTO> entity,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final PersonDTO posted = entity.getBody();
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (issuer.getIdAsInt() == posted.getId()) {
			peopleService.register(posted, version);
			return ResponseEntity.created(
					URI.create("/profiles/"+posted.getId()+"/persons")).build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".person+json", 
			APPLICATION_ACCEPT_PREFIX+".person+xml"},
			path = "/persons")
	@Secured(access = Access.READ_WRITE, scope = { Scope.PERSON_INFO })
	public ResponseEntity<PersonDTO> updatePerson(
			HttpEntity<PersonDTO> entity,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final PersonDTO posted = entity.getBody();
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (issuer.getIdAsInt() == posted.getId() 
				|| securityContext.getToken().isSuper()) {
			peopleService.update(posted, version);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
