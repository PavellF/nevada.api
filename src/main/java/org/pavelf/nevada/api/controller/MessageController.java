package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import org.pavelf.nevada.api.service.PageAndSortExtended;
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
import org.springframework.web.bind.annotation.RequestParam;
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
			@PathVariable("id") int profileId, 
			PageAndSortExtended pageAndSort) {
		
		if (securityContext.isAuthorized()) {
			final User issuer = securityContext.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			
			boolean isSuper = securityContext.getToken().isSuper();
			boolean isOwnerRequesting = issuer.getIdAsInt() == profileId;
			
			if (isSuper || isOwnerRequesting) {
				return ResponseEntity.ok(messageService.getAllForProfile(
						profileId, pageAndSort, true));
			} 
			
			return ResponseEntity.ok(messageService.getAllForProfile(
					profileId, issuer.getIdAsInt(), pageAndSort, false));
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
				if (!messageService
						.isPostedUnderPost(destinationId, repliedId)) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
			} 
			
			if (owner == Owner.PROFILE && streamPostService
					.profileHasPost(ownerId, destinationId)) {

				final URI created = URI.create(
					"profile/"+ownerId+"/posts/"+destinationId+"/messages");
				
				boolean canCommentPost = canCommentPost(
						streamPostService.getById(destinationId, version),
						ownerId, issuerId, posted);
				
				if (canCommentPost || securityContext.getToken().isSuper()) {
					messageService.saveUnderStreamPost(
							destinationId, posted, version);
					return ResponseEntity.created(created).build();
				}
				
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	protected boolean canCommentPost(Optional<StreamPostDTO> maybePost, 
			int ownerId, int issuerId, MessageDTO posted) {
		final StreamPostDTO post = maybePost.orElseThrow(() -> 
					new WebApplicationException(ACCESS_DENIED));
		
		final Visibility whoCanCommentThis = post.getCommentable();
		final boolean isOwner = ownerId == issuerId;
		final Visibility visibilityOfPost = post.getVisibility();
		final boolean isRegularUser = posted.getPriority() == null;
		boolean allowed = false;
			
		if (visibilityOfPost == ALL) {
			
			switch (whoCanCommentThis) {
			case ALL: allowed = isOwner || isRegularUser; break;
			case FRIENDS: allowed = isOwner || (isRegularUser && 
				followersService.isFollow(issuerId, ownerId)); break;
			case ME: allowed = isOwner; break;
			}
			
		} else if (visibilityOfPost == FRIENDS) {
			
			if (whoCanCommentThis == ALL || whoCanCommentThis == FRIENDS) {
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
			
			if (owner == Owner.PROFILE && streamPostService
					.profileHasPost(ownerId, destinationId)) {
				
				final boolean isAuthor = 
						messageService.isAuthorOf(issuerId, posted.getId());
				final boolean canCommentPost = canCommentPost(
						streamPostService.getById(destinationId, version),
						ownerId, issuerId, posted);
				final boolean isOwner = ownerId == issuerId;
				final boolean isAuthorAndOwner = isAuthor && isOwner;
				final boolean isAuthorOfMessageButNotOwnerOfPost = 
						isAuthor && !isOwner && posted.getPriority() == null;
				final boolean isNotAuthorOfMessageButOwnerOfPost = 
						!isAuthor && isOwner && posted.getContent() == null;
				
				if (securityContext.getToken().isSuper() || (canCommentPost
						&& (isAuthorAndOwner || 
								isNotAuthorOfMessageButOwnerOfPost || 
								isAuthorOfMessageButNotOwnerOfPost))) {
					
					messageService.update(posted, version);
					return ResponseEntity.noContent().build();
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	/**
	 * Request parameter 'repliesFor=%id%' return all replies for comment with
	 * given id. If not specified only comments which are not replies 
	 * to other comments will be returned. Returned objects has additional
	 * field describing count of replies.
	 * */
	@GetMapping(produces = { 
		APPLICATION_ACCEPT_PREFIX+".message+json", 
		APPLICATION_ACCEPT_PREFIX+".message+xml"},
		path = "/{owner}/{owner_id}/{destination}/{destination_id}/messages")
	public ResponseEntity<List<MessageDTO>> getMessages(
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestParam(name = "repliesFor",required=false) Integer replyId,
			PageAndSortExtended pageAndSort) {
		
		if (owner == Owner.PROFILE) {
			
			if (destination == Destination.STREAM_POST) {
				
				final StreamPostDTO post = streamPostService.getById(
							destinationId, pageAndSort.getObjectVersion())
						.orElseThrow(() -> 
						new WebApplicationException(ACCESS_DENIED));
				
				if (post.getAssociatedProfile() != ownerId) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
				
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
					
					isAllowed = isSuperOrOwnerRequest || isAllowed;
					
					if (isAllowed) {
						return ResponseEntity.ok(messageService
								.getListOfRepliesForMessageUnderStreamPost(
								destinationId, pageAndSort, issuerId,
								replyId, isSuperOrOwnerRequest));
					}
				}
				
				if (isAllowed) {
					return ResponseEntity.ok(messageService
							.getListOfRepliesForMessageUnderStreamPost(
							destinationId, pageAndSort, replyId, false));
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	/**
	 * GETs all messages under stream post. 
	 * Returned message can be reply to other message or not.
	 * */
	@GetMapping(produces = { APPLICATION_ACCEPT_PREFIX+".message+json", 
	APPLICATION_ACCEPT_PREFIX+".message+xml"},
	path = "/{owner}/{owner_id}/{destination}/{destination_id}/messagesList")
	public ResponseEntity<List<MessageDTO>> getMessages(
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			PageAndSortExtended pageAndSort) {
		
		if (owner == Owner.PROFILE) {
			
			if (destination == Destination.STREAM_POST) {
				
				final StreamPostDTO post = streamPostService
						.getById(destinationId, pageAndSort.getObjectVersion())
						.orElseThrow(() -> 
								new WebApplicationException(ACCESS_DENIED));
				
				if (post.getAssociatedProfile() != ownerId) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
				
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
					
					isAllowed = isSuperOrOwnerRequest || isAllowed;
					
					if (isAllowed) {
						return ResponseEntity.ok(messageService
								.getListOfRepliesForStreamPost(
								destinationId, issuerId, pageAndSort, 
								isSuperOrOwnerRequest));
					}
				}
				
				if (isAllowed) {
					return ResponseEntity.ok(messageService
							.getListOfRepliesForStreamPost(
							destinationId, pageAndSort, false));
				}
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	/**
	 * GETs all messages under stream post only with given ids. 
	 * Returned message can be reply to other message or not.
	 * */
	@GetMapping(produces = { APPLICATION_ACCEPT_PREFIX+".message+json", 
	APPLICATION_ACCEPT_PREFIX+".message+xml"},
	path = "/{owner}/{owner_id}/{destination}/{destination_id}/{ids}")
	public ResponseEntity<List<MessageDTO>> getMessagesByIds(
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@PathVariable("ids") String messageIds,
			PageAndSortExtended pageAndSort) {
		
		List<Integer> ids = Pattern.compile("-").splitAsStream(messageIds)
				.limit(75).map(Integer::valueOf).collect(Collectors.toList());
		
		if (owner == Owner.PROFILE) {
			
			if (destination == Destination.STREAM_POST) {
				
				final StreamPostDTO post = streamPostService
						.getById(destinationId, pageAndSort.getObjectVersion())
							.orElseThrow(() -> 
								new WebApplicationException(ACCESS_DENIED));
				
				if (post.getAssociatedProfile() != ownerId) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
				
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
						isAllowed = followersService
								.isFollow(issuerId, ownerId);
					}
					
					isAllowed = isSuperOrOwnerRequest || isAllowed;
					
					if (isAllowed) {
						return ResponseEntity.ok(messageService
								.getListOfMessagesUnderStreamPost(
										destinationId,pageAndSort, 
										ids, issuerId));
					}
				}
				
				if (isAllowed) {
					return ResponseEntity.ok(messageService
							.getListOfMessagesUnderStreamPost(destinationId, 
									pageAndSort, ids));
				} 
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	/**
	 * GETs map consists of message - reply ids pairs.
	 * */
	@GetMapping(produces = { 
	APPLICATION_ACCEPT_PREFIX+".messageMap+json", 
	APPLICATION_ACCEPT_PREFIX+".messageMap+xml"},
	path = "/{owner}/{owner_id}/{destination}/{destination_id}/messages/map")
	public ResponseEntity<Map<Integer, Integer>> getMessagesMap(
			@PathVariable("owner") Owner owner,
			@PathVariable("owner_id") int ownerId,
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) {
		
		if (owner == Owner.PROFILE) {
			
			if (destination == Destination.STREAM_POST) {
				
				final StreamPostDTO post = streamPostService
						.getById(destinationId, version).orElseThrow(() -> 
							new WebApplicationException(ACCESS_DENIED));
				
				if (post.getAssociatedProfile() != ownerId) {
					throw new WebApplicationException(ACCESS_DENIED);
				}
				
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
						isAllowed = followersService
								.isFollow(issuerId, ownerId);
					}
				}
				
				if (isSuperOrOwnerRequest || isAllowed) {
					return ResponseEntity.ok(messageService
							.getMessageIdReplyIdForStreamPost(
									destinationId, isSuperOrOwnerRequest));
				} 
			}
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
