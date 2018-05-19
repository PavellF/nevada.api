package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.USER_NOT_FOUND;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("\tokens")
public class TokenController {

	private TokenService tokenService;
	private ApplicationService applicationService;
	private TokenContext context;
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".token+json", 
			APPLICATION_ACCEPT_PREFIX+".token+xml"})
	@Secured(access=Access.READ_WRITE, scope = Scope.ACCOUNT)
	public ResponseEntity<TokenDTO> createToken(HttpEntity<TokenDTO> entity) {
		TokenDTO posted = entity.getBody();
		Version version = new VersionImpl(
				entity.getHeaders().getContentType().getParameter("version"));
		
		posted.setVersion(version);
		applicationService.get
		
		
		
		
		User issuedFor = context.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(USER_NOT_FOUND);
		});
		
		posted.setProfileId(Integer.valueOf(issuer.getId()));
		
		Integer id = applicationService.create(posted).getId();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, "/applications/" + id);
		
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
}
