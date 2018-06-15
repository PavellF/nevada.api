package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.service.StreamPostService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamPostController {

	private TokenContext principal;
	private StreamPostService streamPostService;
	private ProfilePreferencesService profilePreferencesService;
	private ProfileService profileService;
	private FollowersService followersService;
	
	@Autowired
	public StreamPostController(TokenContext principal,
			StreamPostService streamPostService,
			ProfilePreferencesService profilePreferencesService,
			ProfileService profileService, FollowersService followersService) {
		this.principal = principal;
		this.streamPostService = streamPostService;
		this.profilePreferencesService = profilePreferencesService;
		this.profileService = profileService;
		this.followersService = followersService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/profile/{id}/posts")	
	@Secured(access = Access.READ, scope = { Scope.STREAM })
	public ResponseEntity<List<StreamPostDTO>> getStreamPosts(
			@PathVariable("id") int id,
			PageAndSort pageAndSort) { 
		
		final User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		if (principal.getToken().isSuper() || issuer.getIdAsInt() == id) {
			return ResponseEntity.ok(streamPostService.
					getAllForAuthor(id, null, pageAndSort));
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")	
	public ResponseEntity<List<StreamPostDTO>> getStreamPostsForDestination(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			PageAndSort pageAndSort) { 
		
		if (destination == Destination.PROFILE) {
			
			if (principal.isAuthorized()) {
				final User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
				
				if (issuer.getIdAsInt() == destinationId || 
				principal.getToken().isSuper()) {
			
				return ResponseEntity.ok(streamPostService.getAllForProfile(
						Integer.valueOf(destinationId), pageAndSort, null));
			
				}
			}
			
			final Integer destId = Integer.valueOf(destinationId);
			final Integer issuerId = issuer.getIdAsInt();
			boolean areFriends = followersService
						.isFollow(issuerId, destId);
			if (areFriends) {
				return ResponseEntity.ok(streamPostService
						.getAllForProfile(destId, pageAndSort, issuerId,
								Visibility.ALL, Visibility.FRIENDS));
			} else {
				return ResponseEntity.ok(streamPostService.getAllForProfile(
						destId, pageAndSort, issuerId, Visibility.ALL));
			}
				
		} else if (destination == Destination.TAG) {
			
			return ResponseEntity.ok(streamPostService
					.getAllForTag(destinationId, pageAndSort, ));
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
				ProfilePreferencesDTO prefs = profilePreferencesService
						.getForProfile(destinationId, version);
				
				switch (prefs.getCanPostOnMyStream()) {
				case ALL: {
					//check if owner blocked this user
					this.streamPostService
					.createOnProfile(posted, destinationId, version);
					return ResponseEntity.created(
							URI.create("profile/"+destinationId+"/posts"))
							.build();
					}
				case FRIENDS: {
					if (followersService.isFollow(issuerId, destinationId)) {
						this.streamPostService.createOnProfile(
								posted, destinationId, version);
						return ResponseEntity.created(
								URI.create("profile/"+destinationId+"/posts"))
								.build();
					}
					} 
				case ME: {
						throw new WebApplicationException(ACCESS_DENIED);
					} 
				}
			}
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM})
	public ResponseEntity<StreamPostDTO> updateStreamPost(
			HttpEntity<StreamPostDTO> entity,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final StreamPostDTO posted = entity.getBody();
		final User issuer = principal.getToken().getUser().orElseThrow(() -> 
			new WebApplicationException(UNRECOGNIZED_USER));
		final int issuerId = issuer.getIdAsInt();
		//check if owner blocked this user
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (principal.getToken().isSuper()) {
			if(streamPostService.update(posted, version)) {
				return ResponseEntity.noContent().build();
			}
			throw new WebApplicationException(FAILED_UPDATE);
		}
		
		if (posted.getPopularity() != null) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		if (Destination.PROFILE == destination) {
			// stream owner can only change priority and visibility
			if (issuerId == destinationId && posted.getContent() == null) {
				if(streamPostService.update(posted, version)) {
					return ResponseEntity.noContent().build();
				}
				throw new WebApplicationException(FAILED_UPDATE);
			}
			//post owner can not change priority
			
			if (streamPostService.belongsTo(issuerId, posted.getId()) 
					&& posted.getPriority() == null) {
				if(streamPostService.update(posted, version)) {
					return ResponseEntity.noContent().build();
				}
				throw new WebApplicationException(FAILED_UPDATE);
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
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
