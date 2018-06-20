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
import org.pavelf.nevada.api.service.FollowersService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code FollowersService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class FollowersServiceImpl implements FollowersService {

	private FollowerRepository followerRepository;
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
	private final Function<? super Sorting, ? extends Order> propertyMapper = 
						(Sorting s) -> {
							if (Sorting.TIME_ASC == s) {
								return Order.asc("id");
							} else if (Sorting.TIME_DESC == s) {
								return Order.desc("id");
							} else {
								return null;
							}
						};
	
	@Autowired		
	public FollowersServiceImpl(FollowerRepository followerRepository) {
		this.followerRepository = followerRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FollowerDTO> getAllFollowers(int profileId, 
			PageAndSortExtended params, boolean mutualOnly) {
		if (params == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		Pageable pageRequest = PageRequest.of(params.getStartIndex(), 
				params.getCount(), sort);
		
		return followerRepository
				.findAllFollowers(profileId, mutualOnly, pageRequest)
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<FollowerDTO> getAllFollowed(int profileId, 
			PageAndSortExtended params, boolean mutualOnly) {
		if (params == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		Pageable pageRequest = PageRequest.of(params.getStartIndex(), 
				params.getCount(), sort);
		
		return followerRepository
				.findAllFollowed(profileId, mutualOnly, pageRequest)
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Integer follow(FollowerDTO follower, Version version,
			boolean mutual) {
		if (follower == null || version == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Follower newFollower = new Follower();
		newFollower.setFollowedId(follower.getFollowedId());
		newFollower.setFollowerId(follower.getFollowerId());
		
		if (mutual) {
			newFollower.setMutual(true);
			newFollower.setSince(Instant.now());
		} else {
			newFollower.setMutual(false);
		}
		
		return followerRepository.save(newFollower).getId();
	}

	@Override
	@Transactional
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
