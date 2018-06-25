package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.Temporal;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.PhotoService;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes endpoints to access {@code StreamPost} resource.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class StreamPostController {

	private TokenContext principal;
	private StreamPostService streamPostService;
	private ProfilePreferencesService profilePreferencesService;
	private ProfileService profileService;
	private FollowersService followersService;
	private PhotoService photoService;
	
	@Autowired
	public StreamPostController(TokenContext principal,
			StreamPostService streamPostService,
			ProfilePreferencesService profilePreferencesService,
			ProfileService profileService, FollowersService followersService,
			PhotoService photoService) {
		this.principal = principal;
		this.streamPostService = streamPostService;
		this.profilePreferencesService = profilePreferencesService;
		this.profileService = profileService;
		this.followersService = followersService;
		this.photoService = photoService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/profile/{id}/posts")	
	@Secured(access = Access.READ, scope = { Scope.STREAM })
	public ResponseEntity<List<StreamPostDTO>> getStreamPosts(
			@PathVariable("id") int id,
			PageAndSortExtended pageAndSort) { 
		
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
			@PathVariable("destination_id") String destinationId,
			PageAndSortExtended pageAndSort) { 
		
		if (destination == Destination.PROFILE) {
			final Integer destId = Integer.valueOf(destinationId);
			
			if (principal.isAuthorized()) {
				final User issuer = principal.getToken().getUser()
						.orElseThrow(UnrecognizedUserException::new);
				final Integer issuerId = issuer.getIdAsInt();
				
				if (issuerId == destId || principal.getToken().isSuper()) {
			
					return ResponseEntity.ok(streamPostService
						.getAllForProfile(destId, pageAndSort));
				}
				
				final boolean areFriends = followersService
						.isFollow(issuerId, destId);
				
				if (areFriends) {
					return ResponseEntity.ok(streamPostService
							.getAllForProfile(destId, pageAndSort, issuerId,
									Visibility.ALL, Visibility.FRIENDS));
				}
			} else {
				return ResponseEntity.ok(streamPostService.getAllForProfile(
						destId, pageAndSort, Visibility.ALL));
			}
				
		} else if (destination == Destination.TAG) {
			
			if (principal.isAuthorized()) {
				final User issuer = principal.getToken().getUser()
						.orElseThrow(UnrecognizedUserException::new);
				final Integer issuerId = issuer.getIdAsInt();
				
				return ResponseEntity.ok(streamPostService
						.getAllForTag(destinationId, pageAndSort, issuerId));
			} else {
				return ResponseEntity.ok(streamPostService
					.getAllForTag(destinationId, pageAndSort));
			}
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	protected void validateAttachments(Collection<Integer> images, 
			Collection<String> tags, int authorId) {
		
		if (images != null) {
			if (images.size() > 16) {
				throw new WebApplicationException(OUT_OF_BOUND_VALUE);
			}
			
			if (!photoService.areBelongsTo(authorId, images)) {
				throw new WebApplicationException(ACCESS_DENIED);
			}
		}
		
		if (tags != null && tags.size() > 24) {
			throw new WebApplicationException(OUT_OF_BOUND_VALUE);
		} 
		
	}
	
	protected ResponseEntity<StreamPostDTO> createPost(StreamPostDTO posted, 
			int destinationId, Version version, int authorId) {
		
		validateAttachments(posted.getImages(), posted.getTags(), authorId);
		streamPostService.createOnProfile(posted, destinationId, version);
		return ResponseEntity.created(URI
				.create("profile/"+destinationId+"/posts")).build();
	}
	
	protected ResponseEntity<StreamPostDTO> updatePost(StreamPostDTO posted, 
			int destinationId, Version version, int authorId) {
		
		validateAttachments(posted.getImages(), posted.getTags(), authorId);
		streamPostService.update(posted, version);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM })
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
				return createPost(posted, destinationId, version, issuerId);
			}
				
			if (posted.getPriority() != null) {
				throw new WebApplicationException(FORBIDDEN_BODY_PROPERTY);
			}
			//check if user allow other users post on his stream
			ProfilePreferencesDTO prefs = profilePreferencesService
					.getForProfile(destinationId, version);
			
			switch (prefs.getCanPostOnMyStream()) {
			case ALL: {
				//check if owner blocked this user
				return createPost(posted, destinationId, version, issuerId);
				}
			case FRIENDS: {
				if (followersService.isFollow(issuerId, destinationId)) {
					return createPost(posted,destinationId,version,issuerId);
				}
				} 
			case ME: {
				throw new WebApplicationException(ACCESS_DENIED);
				} 
			}
			
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/{destination}/{destination_id}/posts")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM })
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
			return updatePost(posted, destinationId, version, issuerId);
		}
		
		if (posted.getPopularity() != null) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		if (Destination.PROFILE == destination) {
			// stream owner can only change priority and visibility
			if (issuerId == destinationId && posted.getContent() == null) {
				return updatePost(posted, destinationId, version, issuerId);
			}
			//post owner can not change priority
			if (streamPostService.belongsTo(issuerId, posted.getId()) 
					&& posted.getPriority() == null) {
				return updatePost(posted, destinationId, version, issuerId);
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@DeleteMapping(path = "/{destination}/{destination_id}/posts/{id}")
	@Secured(access = Access.READ_WRITE, scope = { Scope.STREAM })
	public ResponseEntity<StreamPostDTO> deleteStreamPost(
			@PathVariable("id") int postId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId) {
		final User issuer = principal.getToken().getUser().orElseThrow(() -> 
			new WebApplicationException(UNRECOGNIZED_USER));
		final int issuerId = issuer.getIdAsInt();
		
		if (Destination.PROFILE == destination) {
			if (issuerId == destinationId 
					|| streamPostService.belongsTo(issuerId, postId)
					|| principal.getToken().isSuper()) {
				streamPostService.deleteStreamPost(postId);
				return ResponseEntity.noContent().build();
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	protected int getUserId() {
		User issuer = principal.getToken().getUser().orElseThrow(() -> 
		new WebApplicationException(UNRECOGNIZED_USER));
		return issuer.getIdAsInt();
	}
	
	/**
	 * Returns only selected posts.
	 * */
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/posts/{ids}")	
	public ResponseEntity<List<StreamPostDTO>> getSelectedPosts(
			@PathVariable("ids") String ids, 
			@RequestHeader(HttpHeaders.ACCEPT) Version version) { 
		
		Set<Integer> postIds = Pattern.compile("-")
				.splitAsStream(ids)
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
		
		if (postIds.size() > 35) {
			throw new WebApplicationException(OUT_OF_BOUND_VALUE);
		}
		
		List<StreamPostDTO> list = (principal.isAuthorized()) 
				? streamPostService.getSelectedVisibleByProfile(
					getUserId(), postIds, version)
				: streamPostService.getSelectedVisibleByProfile(
					null, postIds, version);
		
		return ResponseEntity.ok(list);
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".post+json", 
			APPLICATION_ACCEPT_PREFIX+".post+xml"},
			path = "/posts")	
	public ResponseEntity<List<StreamPostDTO>> getFeaturedPosts(
			@RequestParam(name = "interval", defaultValue = "DAY") 
			Temporal forInterval,
			PageAndSortExtended params) { 
		
		List<StreamPostDTO> list = (principal.isAuthorized()) 
				? streamPostService.getAllVisibleByProfile(
						getUserId(), params, forInterval)
				: streamPostService.getAllVisibleByProfile(
						null, params, forInterval);
		
		return ResponseEntity.ok(list);
	}
}
