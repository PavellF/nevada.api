package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;
import java.util.List;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuestController {

	private TokenContext principal;
	private GuestService guestService;
	
	@Autowired
	public GuestController(TokenContext principal, GuestService guestService) {
		this.principal = principal;
		this.guestService = guestService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".guest+json", 
			APPLICATION_ACCEPT_PREFIX+".guest+xml"},
			path = "/{destination}/{destination_id}/guests")	
	public ResponseEntity<List<GuestDTO>> getGuestsForProfile(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version,
			@RequestParam(name = "start", defaultValue= "0") int start,
			@RequestParam(name = "count", defaultValue= "15") int count,
			@RequestParam(name="order", defaultValue="TIME_ASC") 
			Sorting order) { 
		
		if (count > 75) {
			new WebApplicationException(INVALID_REQUEST_PARAM);
		}
		
		if (principal.isAuthorized()) {
			final User issuer = principal.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			
			if (destination == Destination.PROFILE) {
				if (principal.getToken().isSuper() 
						|| issuer.getIdAsInt() == destinationId) {
					return ResponseEntity
							.ok(guestService.getAllGuestsForProfile(
									destinationId, version, start, count, 
									false, order));
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
