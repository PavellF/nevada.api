package org.pavelf.nevada.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static  org.springframework.http.MediaType.*;
import java.security.Principal;


import javax.validation.constraints.Null;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Scope;

import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController()
@RequestMapping("/profiles")
public class ProfileController {

	private TokenContext principal;
	
	@Autowired
	public ProfileController(TokenContext principal) {
		this.principal = principal;
	}
	
	/**
	 * Produces xml or json representation of the user profile.
	 * Expected headers: 
	 * Accept-Language(optional), Accept-Charset(optional), Authorization(optional),
	 * Accept(mandatory, with versioning information: application/xml;version=1.0)
	 * @param id profile id.
	 * */
	@GetMapping(produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_XML_VALUE},
			path = "/{id}")	
	public PersonDTO getProfile(@PathVariable("id") int id, 
			HttpEntity<String> entity) { 
		System.out.println(principal.isAuthorized());
		PersonDTO per = PersonDTO.of(0, null, "", "daw");
		//entity.getHeaders().getAccept().get(0).	
		//System.out.println(principal.getName());
		return per;
	}
	
	/*@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".profile+json", 
			APPLICATION_ACCEPT_PREFIX+".profile+xml"})
	public ResponseEntity<String> postProfile(HttpEntity<Profile> entity) {
		
	}*/
	
	
	
}
