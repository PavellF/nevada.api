package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.LikeDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.LikeRepository;
import org.pavelf.nevada.api.service.LikeService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation of {@code LikeService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class LikeServiceImpl implements LikeService {

	private LikeRepository likeRepository;
	private final Function<Like, LikeDTO> mapper = (Like l) -> {
		LikeDTO like = LikeDTO.builder()
				.withId(l.getId())
				.withLikedBy(l.getLikedById())
				.withLikedMessage(l.getLikedMessage())
				.withLikedStreamPost(l.getLikedStreamPost())
				.withRating(l.getRating())
				.withWhen(l.getDate()).build();
		
		return like;
	};
	
	private final Function<Sorting, Order> propertyMapper = (Sorting s) -> {
				switch (s) {
				case TIME_ASC: return Order.asc("id");
				case TIME_DESC: return Order.desc("id");
				default: return null;
				}
	};
	
	private Pageable getPageable(PageAndSortExtended params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),params.getCount(), sort);
	}
	
	@Autowired
	public LikeServiceImpl(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public LikeDTO getById(int likeId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return likeRepository.findById(likeId).map(mapper).orElse(null);
	}

	@Override
	@Transactional
	public void update(LikeDTO like, Version version) {
		
		final Integer id = like.getId();
		
		if (version == null || like == null || id == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}

		Like toUpdate = new Like();
		toUpdate.setId(id);
		toUpdate.setRating(like.getRating());
		
		likeRepository.save(toUpdate);
	}

	@Override
	@Transactional
	public Integer likePost(LikeDTO like, int streamPostId, Version version) {
		if (version == null || like == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Like toSave = new Like();
		toSave.setDate(Instant.now());
		toSave.setRating(like.getRating());
		toSave.setLikedById(like.getLikedBy());
		toSave.setLikedStreamPost(streamPostId);
		
		return likeRepository.save(toSave).getId();
	}

	@Override
	@Transactional
	public Integer likeMessage(LikeDTO like, int messageId, Version version) {
		if (version == null || like == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Like toSave = new Like();
		toSave.setDate(Instant.now());
		toSave.setRating(like.getRating());
		toSave.setLikedById(like.getLikedBy());
		toSave.setLikedMessage(messageId);
		
		return likeRepository.save(toSave).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<LikeDTO> getAllForProfile(int profileId,
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return likeRepository.findAllForProfile(profileId, getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<LikeDTO> getAllForPost(int postId, 
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return likeRepository.findAllForPost(postId, getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<LikeDTO> getAllForMessage(int messageId,
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return likeRepository.findAllForMessage(messageId, getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean isPostAlreadyLikedByUser(int postId, int profileId) {
		return likeRepository
				.countPostAlreadyLikedByUser(postId, profileId) == 1;
	}

	@Override
	@Transactional
	public boolean isMessageAlreadyLikedByUser(int messageId, int profileId) {
		return likeRepository
				.countMessageAlreadyLikedByUser(messageId, profileId) == 1;
	}

	@Override
	@Transactional
	public boolean belongsTo(int likeId, int profileId) {
		return likeRepository.countLikedByAndLike(likeId, profileId) == 1;
	}

	@Override
	@Transactional
	public void delete(int id) {
		likeRepository.deleteById(id);

	}

}
