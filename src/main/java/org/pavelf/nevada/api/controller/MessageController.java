package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;
import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.Owner;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Visibility;

import static org.pavelf.nevada.api.persistence.domain.Visibility.*;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.service.StreamPostService;
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

/**
 * Exposes some endpoints that can accept and emit {@code Message} objects.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class MessageController {

	private MessageService messageService;
	private TokenContext securityContext;
	private ProfileService profileService;
	private FollowersService followersService;
	private StreamPostService streamPostService;
	
	@Autowired
	public MessageController(MessageService messageService,
			TokenContext securityContext, ProfileService profileService,
			FollowersService followersService,
			StreamPostService streamPostService) {
		this.messageService = messageService;
		this.securityContext = securityContext;
		this.profileService = profileService;
		this.followersService = followersService;
		this.streamPostService = streamPostService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"},
			path = "/profiles/{id}/messages")	
	public ResponseEntity<List<MessageDTO>> getMessagesForAuthor(
			@PathVariable("id") int profileId, PageAndSort pageAndSort) {
		
		if (securityContext.isAuthorized()) {
			final User issuer = securityContext.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			
			boolean isSuper = securityContext.getToken().isSuper();
			boolean isOwnerRequesting = issuer.getIdAsInt() == profileId;
			
			if (isSuper || isOwnerRequesting) {
				return ResponseEntity.ok(messageService.getAllForProfile(
						profileId, pageAndSort, true));
			}
		}
		
		return ResponseEntity.ok(messageService
						.getAllForProfile(profileId, pageAndSort, false));
		
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"},
			path = "/{owner}/{owner_id}/{destination}/{destination_id}/messages")
	@Secured(access = Access.READ_WRITE, scope = { Scope.MESSAGE })
	public ResponseEntity<MessageDTO> postMessage(
			HttpEntity<MessageDTO> entity,
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final MessageDTO posted = entity.getBody();
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
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
		
		if (destination == Destination.STREAM_POST) {
			
			final Integer repliedId = posted.getReplyTo();
			if (repliedId != null) {
				if (!messageService.isPostedUnderPost(destinationId, repliedId)) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			} 
			
			if (owner == Owner.PROFILE && 
					streamPostService.profileHasPost(ownerId, destinationId)) {

				final URI created = URI.create(
						"profile/"+ownerId+"/posts/"+destinationId+"/messages");
				/*	
				final StreamPostDTO post = 
					streamPostService.getById(destinationId, version);
				
				final Visibility whoCanCommentThis = post.getCommentable();
				final boolean isOwner = ownerId == issuerId;
				final boolean isRegularUser = posted.getPriority() == null;
				final Visibility visibilityOfPost = post.getVisibility();
				boolean allowed = false;
				
				if (visibilityOfPost == ALL) {
					
					switch (whoCanCommentThis) {
					case ALL: allowed = isOwner || isRegularUser; break;
					case FRIENDS: allowed = isOwner || (isRegularUser && 
						followersService.isFollow(issuerId, ownerId)); break;
					case ME: allowed = isOwner; break;
					}
					
				} else if (visibilityOfPost == FRIENDS) {
					
					if (whoCanCommentThis == ALL || 
							whoCanCommentThis == FRIENDS) {
						allowed = isOwner || (isRegularUser && 
						followersService.isFollow(issuerId, ownerId));
					} else {
						allowed = isOwner;
					}
					
				} else if (visibilityOfPost == ME) {
					allowed = isOwner;
				}
				*/
				boolean canCommentPost = canCommentPost(
						streamPostService.getById(destinationId, version),
						ownerId, issuerId, posted.getPriority() == null);
				
				if (canCommentPost || securityContext.getToken().isSuper()) {
					messageService.saveUnderStreamPost(
							destinationId, posted, version);
					return ResponseEntity.created(created).build();
				}
				
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	protected boolean canCommentPost(StreamPostDTO post, int ownerId,
			int issuerId, boolean isRegularUser) {
			final Visibility whoCanCommentThis = post.getCommentable();
			final boolean isOwner = ownerId == issuerId;
			final Visibility visibilityOfPost = post.getVisibility();
			boolean allowed = false;
			
			if (visibilityOfPost == ALL) {
				
				switch (whoCanCommentThis) {
				case ALL: allowed = isOwner || isRegularUser; break;
				case FRIENDS: allowed = isOwner || (isRegularUser && 
					followersService.isFollow(issuerId, ownerId)); break;
				case ME: allowed = isOwner; break;
				}
				
			} else if (visibilityOfPost == FRIENDS) {
				
				if (whoCanCommentThis == ALL || 
						whoCanCommentThis == FRIENDS) {
					allowed = isOwner || (isRegularUser && 
					followersService.isFollow(issuerId, ownerId));
				} else {
					allowed = isOwner;
				}
				
			} else if (visibilityOfPost == ME) {
				allowed = isOwner;
			}
			
			return allowed;
	}
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"},
			path = "/{owner}/{owner_id}/{destination}/{destination_id}/messages")
	@Secured(access = Access.READ_WRITE, scope = { Scope.MESSAGE })
	public ResponseEntity<MessageDTO> updateMessage(
			HttpEntity<MessageDTO> entity,
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final MessageDTO posted = entity.getBody();
		final User issuer = securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (profileService.isSuspended(issuerId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		if (destination == Destination.STREAM_POST) {
			
			if (owner == Owner.PROFILE 
					&& streamPostService.profileHasPost(ownerId, destinationId)) {
				
				final boolean isAuthorRequested = 
						messageService.isBelongsTo(issuerId, posted.getId());
				final boolean canCommentPost = canCommentPost(
						streamPostService.getById(destinationId, version),
						ownerId, issuerId, posted.getPriority() == null);
				final boolean isOwner = ownerId == issuerId;
				final boolean isAuthorAndOwner = isAuthorRequested && isOwner;
				
				if (securityContext.getToken().isSuper() || (canCommentPost
						&& (isAuthorAndOwner || 
			(!isAuthorRequested && isOwner && posted.getContent() == null) || 
            (isAuthorRequested && !isOwner && posted.getPriority() == null)))) {
					
					messageService.update(posted, version);
					return ResponseEntity.noContent().build();
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".message+json", 
			APPLICATION_ACCEPT_PREFIX+".message+xml"},
			path = "/{owner}/{owner_id}/{destination}/{destination_id}/messages")
	public ResponseEntity<List<MessageDTO>> getMessages(
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			PageAndSort pageAndSort) {
		
		if (owner == Owner.PROFILE) {
			
			if (destination == Destination.STREAM_POST && 
					streamPostService.profileHasPost(ownerId, destinationId)) {
				
				final StreamPostDTO post = streamPostService.getById(
							destinationId, pageAndSort.getObjectVersion());
				final Visibility visibilityOfPost = post.getVisibility();
				boolean isAllowed = visibilityOfPost == ALL;
				boolean isSuperOrOwnerRequest = false; 
			
				if (securityContext.isAuthorized()) {
					final User issuer = securityContext.getToken().getUser()
						.orElseThrow(UnrecognizedUserException::new);
					final Integer issuerId = issuer.getIdAsInt();
					isSuperOrOwnerRequest = issuerId == ownerId ||
							securityContext.getToken().isSuper();
					
					if (visibilityOfPost == FRIENDS) {
						isAllowed = isSuperOrOwnerRequest || 
							followersService.isFollow(issuerId, ownerId);
					}
				}
				
				if (isSuperOrOwnerRequest || isAllowed) {
					return ResponseEntity.ok(messageService
						.getListForStreamPost(destinationId, 
								pageAndSort, isSuperOrOwnerRequest));
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	
}
