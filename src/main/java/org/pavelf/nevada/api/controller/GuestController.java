package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;
import java.util.List;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.GuestService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines endpoints for {@code Guest} resource.
 * @author Pavel F.
 * @since 1.0
 * */
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
			PageAndSortExtended pageAndSort) { 
		
		if (principal.isAuthorized()) {
			final User issuer = principal.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			final boolean isSuper = principal.getToken().isSuper();
			
			if (destination == Destination.PROFILE) {
				
				if (isSuper || issuer.getIdAsInt() == destinationId) {
					return ResponseEntity.ok(guestService
							.getAllGuestsForProfile(
									destinationId, isSuper, pageAndSort));
				} 
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
