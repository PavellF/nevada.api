package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Tuple;

import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.StreamPostRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.StreamPostService;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code StreamPostService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class StreamPostServiceImpl implements StreamPostService {

	private StreamPostRepository streamPostRepository;
	private MessageParser messageParser;
	private TagsService tagsService;
	private final Function<? super StreamPost,? extends StreamPostDTO> 
	mapper = (StreamPost s) -> {
		StreamPostDTO toMap = StreamPostDTO.builder()
				.withAuthorId(s.getAuthorId())
				.withContent(s.getContent())
				.withAssociatedProfile(s.getAssociatedProfile())
				.withDate(s.getDate())
				.withId(s.getId())
				.withLastChange(s.getLastChange())
				.withPopularity(s.getPopularity())
				.withRating(s.getRating())
				.withVisibility(s.getVisibility())
				.withUserRating(null)
				.withCommentable(s.getCommentable())
				.withPriority(s.getPriority()).build();
		return toMap;
	};
	
	private final Function<? super Sorting, ? extends Order> propertyMapper = 
			(Sorting s) -> {
				switch (s) {
				case POPULARITY_ASC: return Order.asc("popularity");
				case POPULARITY_DESC: return Order.desc("popularity");
				case RATING_ASC: return Order.asc("rating");
				case RATING_DESC: return Order.desc("rating");
				case TIME_ASC: return Order.asc("id");
				case TIME_DESC: return Order.desc("id");
				default: return null;
				}
	};
	
	private final Function<? super Tuple,? extends StreamPostDTO> 
	tupleMapper = (Tuple t) -> {
		StreamPost s = t.get(0, StreamPost.class);
				
		StreamPostDTO.Builder toMap = StreamPostDTO.builder()
				.withAuthorId(s.getAuthorId())
				.withContent(s.getContent())
				.withDate(s.getDate())
				.withId(s.getId())
				.withLastChange(s.getLastChange())
				.withPopularity(s.getPopularity())
				.withRating(s.getRating())
				.withVisibility(s.getVisibility())
				.withCommentable(s.getCommentable())
				.withPriority(s.getPriority());
		
		Like like = t.get(1, Like.class);
		
		if (like != null) {
			toMap.withUserRating(like.getRating());
		}
		
		return toMap.build();
	};
		
	private Pageable getPageable(PageAndSortExtended params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),
					params.getCount(), sort);
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, Visibility... levels) {
		if (params == null || levels == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return this.streamPostRepository.getAllPostsAssociatedWithProfile(
						profileId, Arrays.asList(levels),
						getPageable(params)).stream().map(mapper)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForProfile(int profileId,  
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return this.streamPostRepository.getAllPostsAssociatedWithProfile(
				profileId, getPageable(params)).stream().map(mapper)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForTag(String tag, 
			PageAndSortExtended params, int requestingId) {
		if (tag == null || params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.findAllByTagWithLikeInfo(
				tag, requestingId, getPageable(params))
				.stream().map(tupleMapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForTag(String tag,
			PageAndSortExtended params) {
		if (tag == null || params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.findAllByTag(tag, getPageable(params))
						.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	public List<StreamPostDTO> getAllForAuthor(int authorId, int requestingId,
			PageAndSortExtended params,
			Version version, Visibility... levels) {
		if (levels == null || params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.findAllByAuthorIdWithLikeInfo(
				authorId, Arrays.asList(levels), requestingId, 
				getPageable(params)).stream().map(tupleMapper)
				.collect(Collectors.toList());
	}

	@Override
	public List<StreamPostDTO> getAllForAuthor(int authorId, Version version,
			PageAndSortExtended params, Visibility... levels) {
		if (levels == null || params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.findAllByAuthorId(
				authorId, Arrays.asList(levels), getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<StreamPostDTO> getById(int postId, Version version,
			int requestingId, Visibility... levels) {
		if (levels == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.findByIdWithLikeInfo(postId, requestingId, 
				Arrays.asList(levels)).map(tupleMapper);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<StreamPostDTO> getById(int postId, Version version,
			Visibility... levels) {
		if (levels == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository
				.findById(postId, Arrays.asList(levels)).map(mapper);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, int requestingId,
			Visibility... levels) {
		if (params == null || levels == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return this.streamPostRepository
				.getAllPostsAssociatedWithProfileWithLikeInfo(
						profileId, requestingId, 
						Arrays.asList(levels),
						getPageable(params)).stream().map(tupleMapper)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Integer createOnProfile(StreamPostDTO post, int profileId,
			Version version) {
		
		if (post == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		StreamPost streamPost = new StreamPost();
		streamPost.setAssociatedProfile(profileId);
		streamPost.setAuthorId(post.getAuthorId());
		streamPost.setDate(Instant.now());
		streamPost.setPopularity(0);
		streamPost.setPriority(post.getPriority());
		streamPost.setRating(0);
		streamPost.setVisibility(post.getVisibility());
		streamPost.setCommentable(post.getCommentable());
		
		ParsedMessage pm = this.messageParser.parse(post.getContent());
		
		streamPost.setContent(pm.getParsed());
		
		Integer id = streamPostRepository.save(streamPost).getId();
		
		tagsService.assosiateWithStreamPostAndAddAllTags(
				pm.getMessageTags(), id);
		
		return id;
	}

	@Override
	@Transactional
	public boolean update(StreamPostDTO post, Version version) {
		
		if (post == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		final Integer id = post.getId();
		
		StreamPost streamPost = new StreamPost();
		streamPost.setId(id);
		streamPost.setLastChange(Instant.now());
		streamPost.setPopularity(post.getPopularity());
		streamPost.setPriority(post.getPriority());
		streamPost.setVisibility(post.getVisibility());
		streamPost.setCommentable(post.getCommentable());
		
		ParsedMessage pm = this.messageParser.parse(post.getContent());
		
		streamPost.setContent(pm.getParsed());
		streamPostRepository.save(streamPost);
		
		tagsService.clearAllStreamPostTags(id);
		
		tagsService.assosiateWithStreamPostAndAddAllTags(
				pm.getMessageTags(),id);
		return true;
	}

	@Override
	public void deleteStreamPost(int postId) {
		streamPostRepository.deleteById(postId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean belongsTo(int profileId, int postId) {
		return streamPostRepository
				.countPostBelongAuthor(postId, profileId) == 1;
	}

	@Override
	public boolean profileHasPost(int profileId, int postId) {
		return streamPostRepository
				.countProfileHasPost(postId, profileId) == 1;
	}


}
