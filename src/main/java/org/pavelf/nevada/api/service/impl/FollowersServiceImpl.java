package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.FollowerDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Follower;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.FollowerRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.FollowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FollowersServiceImpl implements FollowersService {

	private FollowerRepository followerRepository;
	private ProfileRepository profileRepository;
	private final Function<? super Follower, ? extends FollowerDTO> mapper = 
			(Follower f) -> {
				FollowerDTO follower = FollowerDTO.builder()
						.withFollowedId(f.getFollowedId())
						.withFollowerId(f.getFollowerId())
						.withId(f.getId())
						.withMutual(f.isMutual())
						.withSince(f.getSince())
						.build();
				return follower;
				};
	
	@Autowired		
	public FollowersServiceImpl(FollowerRepository followerRepository,
			ProfileRepository profileRepository) {
		this.followerRepository = followerRepository;
		this.profileRepository = profileRepository;
	}

	@Override
	public List<FollowerDTO> getAllFollowers(int profileId, Version version,
			int start, int count, Sorting sorting, boolean mutualOnly) {
		if (sorting == null || version == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Pageable pageRequest = PageRequest.of(start, count);
		Sort sort = Sort.by(sorting.getDirection(), 
				sorting.getDomainProperty());
		
		return followerRepository
				.findAllFollowers(profileId, mutualOnly, pageRequest, sort)
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	public List<FollowerDTO> getAllFollowed(int profileId, Version version,
			int start, int count, Sorting sorting, boolean mutualOnly) {
		if (sorting == null || version == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Pageable pageRequest = PageRequest.of(start, count);
		Sort sort = Sort.by(sorting.getDirection(), 
				sorting.getDomainProperty());
		
		return followerRepository
				.findAllFollowed(profileId, mutualOnly, pageRequest, sort)
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	public Integer follow(FollowerDTO follower, Version version,
			boolean mutual) {
		if (follower == null || version == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Follower newFollower = new Follower();
		newFollower.setFollowed(
				profileRepository.getOne(follower.getFollowedId()));
		newFollower.setFollower(
				profileRepository.getOne(follower.getFollowerId()));
		
		if (mutual) {
			newFollower.setMutual(true);
			newFollower.setSince(Instant.now());
		} else {
			newFollower.setMutual(false);
		}
		
		return followerRepository.save(newFollower).getId();
	}

	@Override
	public void acceptFollower(FollowerDTO follower, Version version) {
		if (follower == null || version == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Follower newFollower = new Follower();
		newFollower.setId(follower.getId());
		newFollower.setMutual(true);
		newFollower.setSince(Instant.now());
		followerRepository.save(newFollower);

	}

	@Override
	public boolean hasRelationship(int followerId, int followedId) {
		return followerRepository
				.hasRelationship(followerId, followedId) == 1;
	}

	@Override
	public boolean followerOrFollowed(int profileId, int id) {
		return followerRepository.followerOrFollowed(profileId, id) == 1;
	}

	@Override
	public boolean isFollow(int followerId, int followedId) {
		return followerRepository
				.isFollow(followerId, followedId) == 1;
	}

	@Override
	public void deleteFollowing(int id) {
		followerRepository.deleteById(id);

	}

}
