package org.pavelf.nevada.api.controller;

import static org.pavelf.nevada.api.Application.APPLICATION_ACCEPT_PREFIX;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import java.net.URI;
import java.util.List;

import org.pavelf.nevada.api.domain.Access;
import org.pavelf.nevada.api.domain.FollowerDTO;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.exception.UnrecognizedUserException;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.security.Secured;
import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.User;
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
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

@RestController
public class FollowerController {

	private TokenContext principal;
	private FollowersService followersService;
	private ProfilePreferencesService profilePreferencesService;
	private ProfileService profileService;
	
	@Autowired
	public FollowerController(TokenContext principal,
			FollowersService followersService,
			ProfilePreferencesService profilePreferencesService,
			ProfileService profileService) {
		this.principal = principal;
		this.followersService = followersService;
		this.profilePreferencesService = profilePreferencesService;
		this.profileService = profileService;
	}

	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".follower+json", 
			APPLICATION_ACCEPT_PREFIX+".follower+xml"},
			path = "/profiles/{owner_id}/followed")
	public ResponseEntity<List<FollowerDTO>>getFollowedProfiles(
			@PathVariable("owner_id") int id,
			@RequestParam(name = "start", defaultValue= "0") int start,
			@RequestParam(name = "count", defaultValue= "5") int count,
			@RequestHeader(HttpHeaders.ACCEPT) Version version,
			@RequestParam(name = "order", defaultValue= "TIME_ASC") Sorting order,
			@RequestParam(name = "mutual", defaultValue= "true") boolean mutual) {
		
		if (count > 100) {
			new WebApplicationException(INVALID_REQUEST_PARAM);
		}
		
		if (principal.isAuthorized()) {
			
			User issuer = principal.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			
			if (!mutual && issuer.getIdAsInt() == id) {
				return ResponseEntity.
						ok(followersService.getAllFollowed(
								id, version, start, count, order, false));
			}
		} 
		//have not accepted friends only followed user allowed to request 
		if (!mutual) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		return ResponseEntity.ok(followersService
						.getAllFollowed(
								id, version, start, count, order, true));
	}
	
	@GetMapping(produces = { 
			APPLICATION_ACCEPT_PREFIX+".follower+json", 
			APPLICATION_ACCEPT_PREFIX+".follower+xml"},
			path = "/profiles/{owner_id}/followers")
	public ResponseEntity<List<FollowerDTO>>getFollowersForProfile(
			@PathVariable("owner_id") int id,
			@RequestParam(name = "start", defaultValue= "0") int start,
			@RequestParam(name = "count", defaultValue= "5") int count,
			@RequestHeader(HttpHeaders.ACCEPT) Version version,
			@RequestParam(name = "order", defaultValue= "TIME_ASC") Sorting order,
			@RequestParam(name = "mutual", defaultValue= "true") boolean mutual) {
		
		if (count > 100) {
			new WebApplicationException(INVALID_REQUEST_PARAM);
		}
		
		if (principal.isAuthorized()) {
			
			User issuer = principal.getToken().getUser()
					.orElseThrow(UnrecognizedUserException::new);
			
			if (!mutual && issuer.getIdAsInt() == id) {
				return ResponseEntity.
						ok(followersService.getAllFollowers(
								id, version, start, count, order, false));
			}
		} 
		//have not accepted friends only followed user allowed to request 
		if (!mutual) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		return ResponseEntity.ok(followersService
						.getAllFollowers(
								id, version, start, count, order, true));
	}
	
	@PostMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".follower+json", 
			APPLICATION_ACCEPT_PREFIX+".follower+xml"},
			path = "/followers")
	@Secured(access = Access.READ_WRITE, scope = { Scope.FRIENDS })
	public ResponseEntity<FollowerDTO> followProfile(
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version,
			HttpEntity<FollowerDTO> entity) {
		final FollowerDTO posted = entity.getBody();
		User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		final int issuerId = issuer.getIdAsInt();
		
		if (profileService.isSuspended(issuerId)) {
			throw new WebApplicationException(BANNED_PROFILE); 
		}
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (posted.getFollowerId() != issuerId) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
		
		if (posted.getFollowedId() == posted.getFollowerId()) {
			throw new WebApplicationException(INVALID_BODY_PROPERTY);
		}
		
		ProfilePreferencesDTO prefs = 
				profilePreferencesService
				.getForProfile(posted.getFollowedId(), version);
		
		followersService
		.follow(posted, version, !prefs.isPremoderateFollowers());
		
		return ResponseEntity.created(
				URI.create("/profiles/"+posted.getFollowedId() + "/followers"))
				.build();
	}
	
	@PutMapping(consumes = {
			APPLICATION_ACCEPT_PREFIX+".follower+json", 
			APPLICATION_ACCEPT_PREFIX+".follower+xml"},
			path = "/followers")
	@Secured(access = Access.READ_WRITE, scope = { Scope.FRIENDS })
	public ResponseEntity<FollowerDTO> acceptFollower(
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Version version,
			HttpEntity<FollowerDTO> entity) {
		final FollowerDTO posted = entity.getBody();
		User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		final int issuerId = issuer.getIdAsInt();
		
		if (posted == null) {
			throw new WebApplicationException(BODY_REQUIRED);
		}
		
		if (posted.getFollowedId() != issuerId) {
			throw new WebApplicationException(ACCESS_DENIED);
		}
	
		if (followersService
				.hasRelationship(posted.getFollowerId(), issuerId)) {
			followersService.acceptFollower(posted, version);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	@DeleteMapping(path = "/followers/{id}")
	@Secured(access = Access.READ_WRITE, scope = { Scope.FRIENDS })
	public ResponseEntity<FollowerDTO> deleteFollower(
			@PathVariable("id") int id) {
		User issuer = principal.getToken().getUser()
				.orElseThrow(UnrecognizedUserException::new);
		
		if (followersService.followerOrFollowed(issuer.getIdAsInt(), id)) {
			followersService.deleteFollowing(id);
			return ResponseEntity.noContent().build();
		}
		
		throw new WebApplicationException(ACCESS_DENIED);
	}
	
	
}
