package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.LikeDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.LikeService;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.StreamPostService;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Exposes endpoints to access {@code Like} resource.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class LikeController {

	private TokenContext securityContext;
	private LikeService likeService;
	private StreamPostService streamPostService;
	private FollowersService followersService;
	private MessageService messageService;
	
	@Autowired
	public LikeController(TokenContext securityContext,
			LikeService likeService,StreamPostService streamPostService,
			FollowersService followersService, MessageService messageService) {
		this.securityContext = securityContext;
		this.likeService = likeService;
		this.streamPostService = streamPostService;
		this.followersService = followersService;
		this.messageService = messageService;
	}

	protected User getCurrentUser() {
		return securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
	}
	
	protected StreamPostDTO getStreamPost(int id, Version version) {
		return streamPostService.getById(id, version).orElseThrow(() -> 
				new WebApplicationException(ACCESS_DENIED));
	}
	
	protected boolean isSuper() {
		return securityContext.getToken().isSuper();
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".like+json", 
			APPLICATION_ACCEPT_PREFIX+".like+xml"},
			path = "/profiles/{id}/likes")	
	public ResponseEntity<List<LikeDTO>> getLikePostedByProfile(
			@PathVariable("id") int profileId, 
			PageAndSortExtended params) {
		
		return ResponseEntity
				.ok(likeService.getAllForProfile(profileId, params));
	}
	
	@PostMapping(consumes = { 
			APPLICATION_ACCEPT_PREFIX+".like+json", 
			APPLICATION_ACCEPT_PREFIX+".like+xml"},
			path = "/{destination}/{destination_id}/likes")
	@Secured(access = Access.READ_WRITE, scope = {Scope.MESSAGE, Scope.STREAM})
	public ResponseEntity<Void> postLike(
			HttpEntity<LikeDTO> entity,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final LikeDTO posted = entity.getBody();
		final User issuer = getCurrentUser();	
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (posted.getLikedBy() != issuerId) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		Integer id = null;
		
		if (destination == Destination.STREAM_POST) {
			
			if(likeService.isPostAlreadyLikedByUser(destinationId, issuerId)) {
				throw new WebApplicationException(CAN_NOT_LIKE);
			}
			
			final StreamPostDTO post = getStreamPost(destinationId, version);
			final Integer ownerId = post.getAssociatedProfile();
			final boolean isSuperOrOwner =  isSuper() || ownerId == issuerId;
			boolean allowed = false;
			
			if (post.getAuthorId() == issuerId) {
				throw new WebApplicationException(CAN_NOT_LIKE);
			}
			
			switch (post.getVisibility()) {
				case ALL: allowed = true; break;
				case FRIENDS: allowed = followersService
						.isFollow(issuerId, ownerId) || isSuperOrOwner; break;
				case ME: allowed = isSuperOrOwner; break;
			}
			
			if (allowed) {
				id = likeService.likePost(posted, destinationId, version);
			}
			
		} else if (destination == Destination.MESSAGE) {
			
			if(likeService.isMessageAlreadyLikedByUser(
					destinationId, issuerId) || messageService
					.isAuthorOf(issuerId, destinationId)) {
				throw new WebApplicationException(CAN_NOT_LIKE);
			}
			
			id = likeService.likeMessage(posted, destinationId, version);
		}
		
		if (id != null) {
			return ResponseEntity.created(URI.create(id.toString())).build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@PutMapping(consumes = { 
			APPLICATION_ACCEPT_PREFIX+".like+json", 
			APPLICATION_ACCEPT_PREFIX+".like+xml"},
			path = "/{destination}/{destination_id}/likes")
	@Secured(access = Access.READ_WRITE, scope = {Scope.MESSAGE, Scope.STREAM})
	public ResponseEntity<Void> updateLike(
			HttpEntity<LikeDTO> entity,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final LikeDTO posted = entity.getBody();
		final User issuer = getCurrentUser();	
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		final Integer likeId = posted.getId();
				
		if (likeId == null) {
			throw new WebApplicationException(REQUIRED_BODY_PROPERTY);
		}
		
		boolean allowed = false;
		final LikeDTO like = likeService.getById(likeId, version);
		
		if (like == null || like.getLikedBy() != issuerId) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		if (destination == Destination.STREAM_POST) {
			
			if (like.getLikedStreamPost() == destinationId) {
				final StreamPostDTO post = 
						getStreamPost(destinationId, version);
				final Integer ownerId = post.getAssociatedProfile();
				final boolean isSuperOrOwner =  isSuper() 
						|| ownerId == issuerId;
				
				switch (post.getVisibility()) {
					case ALL: allowed = true; break;
					case FRIENDS: allowed = followersService.isFollow(
							issuerId, ownerId) || isSuperOrOwner; break;
					case ME: allowed = isSuperOrOwner; break;
				}
			}
			
		} else if (destination == Destination.MESSAGE) {
			
			allowed = like.getLikedMessage() == destinationId;
			
		}
		
		if (allowed) {
			likeService.update(posted, version);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".like+json", 
			APPLICATION_ACCEPT_PREFIX+".like+xml"},
			path = "/{destination}/{destination_id}/likes")
	public ResponseEntity<List<LikeDTO>> getLikes(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId, 
			PageAndSortExtended params) {
		
		List<LikeDTO> likes = null;
		final Version version = params.getObjectVersion();
		
		if (destination == Destination.STREAM_POST) {
			
			boolean allowed = false;
			final StreamPostDTO post = getStreamPost(destinationId, version);
			final Integer ownerId = post.getAssociatedProfile();
			final Visibility visibility = post.getVisibility();
			allowed = visibility == Visibility.ALL;
			
			if (securityContext.isAuthorized()) {
				final User issuer = getCurrentUser();	
				final int issuerId = issuer.getIdAsInt();
				final boolean isSuperOrOwner =  isSuper() 
						|| ownerId == issuerId;
				
				if (visibility == Visibility.FRIENDS) {
					
					allowed = followersService.isFollow(issuerId, ownerId) 
							|| isSuperOrOwner;
					
				} else if (visibility == Visibility.ME) {
					allowed = isSuperOrOwner;
				}
			}
			
			if (allowed) {
				likes = likeService.getAllForPost(destinationId, params);
			}
		
		} else if (destination == Destination.MESSAGE) {
			
			likes = likeService.getAllForMessage(destinationId, params);
			
		}
		
		if (likes == null) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		return ResponseEntity.ok(likes);
	}
	
	@DeleteMapping(path = "/likes/{id}")
	@Secured(access = Access.READ_WRITE, scope = {Scope.MESSAGE, Scope.STREAM})
	public ResponseEntity<Void> deleteLike(@PathVariable("id") int id) {
		
		final User issuer = getCurrentUser();	
		final int issuerId = issuer.getIdAsInt();
		
		if (likeService.belongsTo(id, issuerId) || isSuper()) {
			likeService.delete(id);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
