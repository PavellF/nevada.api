package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;
import org.mockito.internal.util.collections.Sets;
import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.PhotoDTO;
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
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.PhotoService;
import org.pavelf.nevada.api.service.PhotoService.Size;
import org.pavelf.nevada.api.service.ProfileService;
import org.pavelf.nevada.api.service.StreamPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes endpoints to access {@link #Photo} resource.
 * @author Pavel F.
 * @since 1.0
 * */
@RestController
public class PhotoController {

	private TokenContext securityContext;
	private PhotoService photoService;
	private FollowersService followersService;
	private StreamPostService streamPostService;
	private ProfileService profileService;
	
	private static final String JSON = APPLICATION_ACCEPT_PREFIX+".photo+json";
	private static final String XML = APPLICATION_ACCEPT_PREFIX+".photo+xml";
	
	@Autowired
	public PhotoController(TokenContext securityContext,
			PhotoService photoService, FollowersService followersService,
			StreamPostService streamPostService,
			ProfileService profileService) {
		this.securityContext = securityContext;
		this.photoService = photoService;
		this.followersService = followersService;
		this.streamPostService = streamPostService;
		this.profileService = profileService;
	}

	protected User getCurrentUser() {
		return securityContext.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
	}
	
	protected boolean isSuperUser() {
		return securityContext.getToken().isSuper();
	}
	
	@GetMapping(produces = { JSON , XML }, 
			path = "/{destination}/{destination_id}/photos")	
	public ResponseEntity<List<PhotoDTO>> getPhotos(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId, 
			PageAndSortExtended pageAndSort) {
		
		if (Destination.PROFILE == destination) {
			if (securityContext.isAuthorized()) {
				final User issuer = getCurrentUser();
				final int issuerId = issuer.getIdAsInt();
				
				if (issuerId == destinationId || isSuperUser()) {
					return ResponseEntity.ok(photoService
						.getAllForProfile(destinationId, pageAndSort, null));
				}
				
				if (followersService.isFollow(issuerId, destinationId)) {
					return ResponseEntity.ok(photoService.getAllForProfile(
							destinationId, pageAndSort,
							Sets.newSet(Visibility.ALL, Visibility.FRIENDS)));
				}
			}
			
			return ResponseEntity.ok(photoService.getAllForProfile(
					destinationId, pageAndSort, Sets.newSet(Visibility.ALL)));
		} else if (Destination.MESSAGE == destination) {
			
			return ResponseEntity.ok(photoService
					.getAllForMessage(destinationId, pageAndSort));
			
		} else if (Destination.STREAM_POST == destination) {
			
			final StreamPostDTO post = streamPostService.getById(
					destinationId, pageAndSort.getObjectVersion())
				.orElseThrow(() -> new WebApplicationException(ACCESS_DENIED));
			boolean allowed = post.getVisibility() == Visibility.ALL;
					
			if (securityContext.isAuthorized()) {
				final User issuer = getCurrentUser();
				final int issuerId = issuer.getIdAsInt();
				
				if (post.getAssociatedProfile() == issuerId || 
						post.getAssociatedProfile() == issuerId || 
						securityContext.getToken().isSuper()) {
					return ResponseEntity.ok(photoService
							.getAllForStreamPost(destinationId, pageAndSort));
				}
				
				allowed = post.getVisibility() == Visibility.FRIENDS && 
						followersService.isFollow(
								issuerId, post.getAssociatedProfile());
			}
			
			if (allowed) {
				return ResponseEntity.ok(photoService
						.getAllForStreamPost(destinationId, pageAndSort));
			}
			
			throw new WebApplicationException(ACCESS_DENIED);
		} 
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE,
			path = "/{destination}/{destination_id}/photos/{id}")	
	public ResponseEntity<byte[]> getImage(
			@PathVariable("destination") Destination destination,
			@PathVariable("destination_id") int destinationId, 
			@PathVariable("id") int id,
			@RequestParam(name = "size", defaultValue = "SMALL") Size size,
			@RequestHeader(HttpHeaders.ACCEPT) Version version) {
		
		if (Destination.PROFILE == destination) {
					
			if (securityContext.isAuthorized()) {
				final User issuer = getCurrentUser();
				final int issuerId = issuer.getIdAsInt();
				
				if (issuerId == destinationId || isSuperUser()) {
					return ResponseEntity.ok(photoService
							.getRawImageById(id, size, null));
				}
				
				if (followersService.isFollow(issuerId, destinationId)) {
					return ResponseEntity.ok(photoService.getRawImageById(
							id, size, Sets
							.newSet(Visibility.ALL, Visibility.FRIENDS)));
				}
			}
			
			return ResponseEntity.ok(photoService.getRawImageById(
					id, size, Sets.newSet(Visibility.ALL)));
		} else if (Destination.MESSAGE == destination) {
			
			return ResponseEntity.ok(photoService
					.getRawImageById(id, size, null));
			
		} else if (Destination.STREAM_POST == destination) {
			
			final StreamPostDTO post = streamPostService.getById(
					destinationId, version).orElseThrow(() -> 
					new WebApplicationException(ACCESS_DENIED));
			boolean allowed = post.getVisibility() == Visibility.ALL;
					
			if (securityContext.isAuthorized()) {
				final User issuer = getCurrentUser();
				final int issuerId = issuer.getIdAsInt();
				
				if (post.getAssociatedProfile() == issuerId || 
						post.getAssociatedProfile() == issuerId || 
						securityContext.getToken().isSuper()) {
					return ResponseEntity.ok(photoService
							.getRawImageById(id, size, null));
				}
				
				allowed = post.getVisibility() == Visibility.FRIENDS && 
						followersService.isFollow(
								issuerId, post.getAssociatedProfile());
			}
			
			if (allowed) {
				return ResponseEntity.ok(photoService
						.getRawImageById(id, size, null));
			}
			
			throw new WebApplicationException(ACCESS_DENIED);
		} 
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			path = "/photos")	
	@Secured(access = Access.READ_WRITE, scope = { Scope.PHOTO })
	public ResponseEntity<byte[]> postImage(
			HttpEntity<byte[]> image,
			@RequestParam(name = "message", required = false) String message,
			@RequestParam(name = "filename", required = false) String fileName,
			@RequestParam(name = "visibility", defaultValue = "ME") 
			Visibility visibility,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final byte[] posted = image.getBody();
		final User issuer = getCurrentUser();	
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (profileService.isSuspended(issuerId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		PhotoDTO photo = PhotoDTO.builder()
				.withFileName(fileName)
				.withMessage(message)
				.withOwnerId(issuerId)
				.withVisibility(visibility).build();
		
		Integer id = photoService.post(photo, posted, version);
		
		return ResponseEntity.created(URI.create(id.toString())).build();
	}
	
	@PutMapping(consumes = { JSON , XML }, path = "/photos")
	@Secured(access = Access.READ_WRITE, scope = { Scope.PHOTO })
	public ResponseEntity<PhotoDTO> updatePhoto(
				HttpEntity<PhotoDTO> entity,
				@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version) {
		
		final PhotoDTO posted = entity.getBody();
		final User issuer = getCurrentUser();	
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (profileService.isSuspended(issuerId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		if (photoService.isBelongsTo(issuerId, posted.getId())
				|| isSuperUser()) {
			photoService.update(posted, version);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
}
