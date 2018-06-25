package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Tuple;

import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.StreamPostRepository;
import org.pavelf.nevada.api.persistence.repository.Temporal;
import org.pavelf.nevada.api.service.AttachmentService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.StreamPostService;
import org.springframework.beans.factory.annotation.Autowired;
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
	private AttachmentService attachmentService;
	
	private final Function<StreamPost,StreamPostDTO> mapper = 
			(StreamPost s) -> {
		StreamPostDTO.Builder toMap = StreamPostDTO.builder()
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
				.withPriority(s.getPriority());
		
		
		Stream<Attachment> attachments = attachmentService
				.getAllStreamPostAttachments(s.getId()).stream();
		
		Set<Integer> photos = attachments.map(Attachment::getPhotoId)
				.collect(Collectors.toSet());
		Set<String> tags = attachments.map(Attachment::getTagName)
				.collect(Collectors.toSet());
		
		toMap
		.withImages(photos)
		.withTags(tags);
		
		return toMap.build();
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
		
		Stream<Attachment> attachments = attachmentService
				.getAllStreamPostAttachments(s.getId()).stream();
		
		Set<Integer> photos = attachments.map(Attachment::getPhotoId)
				.collect(Collectors.toSet());
		Set<String> tags = attachments.map(Attachment::getTagName)
				.collect(Collectors.toSet());
		
		toMap
		.withImages(photos)
		.withTags(tags);
		
		Like like = t.get(1, Like.class);
		
		if (like != null) {
			toMap.withUserRating(like.getRating());
		}
		
		return toMap.build();
	};
	
	@Autowired
	public StreamPostServiceImpl(StreamPostRepository streamPostRepository,
			AttachmentService attachmentService) {
		this.streamPostRepository = streamPostRepository;
		this.attachmentService = attachmentService;
	}

	private Pageable getPageable(PageAndSortExtended params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),params.getCount(), sort);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, Visibility... levels) {
		if (params == null || levels == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return streamPostRepository.getAllPostsAssociatedWithProfile(
				profileId, Arrays.asList(levels), getPageable(params))
				.stream().map(mapper)
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
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
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
		streamPost.setContent(post.getContent());
		
		Integer id = streamPostRepository.save(streamPost).getId();
		Collection<String> tags = post.getTags();
		Collection<Integer> photos = post.getImages();
		
		attachmentService.assosiateWithStreamPost(tags, photos, id);
		
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
		streamPost.setContent(post.getContent());
		streamPostRepository.save(streamPost);
		
		Collection<String> tags = post.getTags();
		Collection<Integer> photos = post.getImages();
		
		attachmentService.updateStreamPostAttachments(id, tags, photos);
		return true;
	}

	@Override
	@Transactional
	public void deleteStreamPost(int postId) {
		attachmentService.deleteAllAttachmentsOnStreamPost(postId);
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

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllVisibleByProfile(Integer profileId,
			PageAndSortExtended params, Temporal forInterval) {
		if (params == null || forInterval == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		final Instant now = Instant.now();
		Instant start;
		
		switch (forInterval) {
			case DAY: start = now.minus(1L, ChronoUnit.DAYS); break;
			case HOUR: start = now.minus(1L, ChronoUnit.HOURS); break;
			case MONTH: start = now.minus(1L, ChronoUnit.MONTHS); break;
			case WEEK: start = now.minus(1L, ChronoUnit.WEEKS); break;
			default: start = null; break;
		}
		
		if (profileId == null && start == null) {
			
			return streamPostRepository.findAllVisible(getPageable(params))
					.stream().map(mapper).collect(Collectors.toList());
			
		} else if (profileId == null && start != null) {
			
			return streamPostRepository.findAllVisibleForInterval(
					start, now, getPageable(params))
					.stream().map(mapper).collect(Collectors.toList());
			
		} else if (profileId != null && start == null) {
			
			return streamPostRepository.findAllForProfile(
					profileId, getPageable(params)).stream()
					.map(tupleMapper).collect(Collectors.toList());
			
		} else if (profileId != null && start != null) {
			
			return streamPostRepository.findAllForProfileForInterval(
					profileId, start, now, getPageable(params)).stream()
					.map(tupleMapper).collect(Collectors.toList());
			
		}
		
		return Collections.emptyList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getSelectedVisibleByProfile(Integer profileId,
			Set<Integer> postIds, Version version) {
		
		if (version == null || postIds == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		if (profileId == null) {
			 
			return streamPostRepository.findAllSelected(postIds)
					.stream().map(mapper).collect(Collectors.toList());
			
		} else {
			
			return streamPostRepository.findAllSelectedForProfile(
					profileId, postIds).stream()
					.map(tupleMapper).collect(Collectors.toList());
			
		}
		
	}

}
