package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.Owner;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.service.StreamPostService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamPostController {

	private TokenContext principal;
	private StreamPostService streamPostService;
	private ProfilePreferencesService profilePreferencesService;
	private ProfileService profileService;
	//getall for given author
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/profile/{id}/posts")	
	@Secured(access = Access.READ, scope = { Scope.STREAM})
	public ResponseEntity<List<StreamPostDTO>> getStreamPosts(
			@PathVariable("id") int id,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		final User issuer = principal.getToken().getUser().orElseThrow(() -> 
		new WebApplicationException(UNRECOGNIZED_USER));
		
		if (principal.getToken().isSuper() || issuer.getIdAsInt() == id) {
			return ResponseEntity.ok(streamPostService.getAllForAuthor(id, version));
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")	
	public ResponseEntity<List<StreamPostDTO>> getStreamPostsForDestination(
			@PathVariable("owner") Owner owner,
			@PathVariable("id") int ownerId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		
		final User issuer = principal.getToken().getUser().orElseThrow(() -> 
		new WebApplicationException(UNRECOGNIZED_USER));
		
		if (owner == Owner.PROFILE) {
			if (issuer.getIdAsInt() == ownerId || principal.getToken().isSuper()) {
				return ResponseEntity.ok(streamPostService.getAllForProfile(ownerId, version));
			} else {
				boolean areFriends = false; 
				if (areFriends) {
					
				} else {
					
				}
				
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM})
	public ResponseEntity<StreamPostDTO> postStreamPost(
			HttpEntity<StreamPostDTO> entity,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final StreamPostDTO posted = entity.getBody();
		final User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (posted.getAuthorId() != issuerId) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		if (profileService.isSuspended(issuerId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		if (destination == Destination.PROFILE) {
			
			boolean isDestinationOwner = destinationId == issuerId;
			
			if (isDestinationOwner || principal.getToken().isSuper()) {
				this.streamPostService.createOnProfile(posted, destinationId, version);
				return ResponseEntity.created(URI.create("profile/"+destinationId+"/posts")).build();
				
			} else {
				
				if (posted.getPriority() != null) {
					throw new WebApplicationException(FORBIDDEN_BODY_PROPERTY);
				}
				//check if user allow other users post on his stream
				ProfilePreferencesDTO prefs = 
						profilePreferencesService.getForProfile(destinationId, version);
				
				switch (prefs.getCanPostOnMyStream()) {
				case ALL: {
					//check if owner blocked this user
					this.streamPostService.createOnProfile(posted, destinationId, version);
					return ResponseEntity.created(URI.create("profile/"+destinationId+"/posts")).build();
					}
				case FRIENDS: {
					//check if users are friends
					} break;
				case ME: {
						throw new WebApplicationException(ACCESS_DENIED);
					} 
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM})
	public ResponseEntity<StreamPostDTO> updateStreamPost(
			HttpEntity<StreamPostDTO> entity,
			@PathVariable("owner") Owner owner,
			@PathVariable("id") int ownerId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		final User issuer = principal.getToken().getUser().orElseThrow(() -> {
			return new WebApplicationException(UNRECOGNIZED_USER);
		});
		return null;
	}
	
	@DeleteMapping(path = "/{destination}/{destination_id}/posts/{id}")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM})
	public ResponseEntity<StreamPostDTO> deleteStreamPost(
			@PathVariable("id") int postId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId) {
		final User issuer = principal.getToken().getUser().orElseThrow(() -> 
		new WebApplicationException(UNRECOGNIZED_USER));
		final int issuerId = issuer.getIdAsInt();
		
		if (Destination.PROFILE == destination) {
			if (issuerId == destinationId || streamPostService.belongsTo(issuerId, postId)
					|| principal.getToken().isSuper()) {
				streamPostService.deleteStreamPost(postId);
				return ResponseEntity.noContent().build();
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	
	
	
	
	
	
	
	
	
	
}
