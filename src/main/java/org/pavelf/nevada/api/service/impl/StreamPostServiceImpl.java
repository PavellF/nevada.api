package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.StreamPostRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.StreamPostService;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	private ProfileRepository profileRepository;
	private MessageParser messageParser;
	private TagsService tagsService;
	private final Function<? super StreamPost,? extends StreamPostDTO> mapper = 
			(StreamPost s) -> {
		StreamPostDTO toMap = StreamPostDTO.builder()
				.withAuthorId(s.getAuthorId())
				.withContent(s.getContent())
				.withDate(s.getDate())
				.withId(s.getId())
				.withLastChange(s.getLastChange())
				.withPopularity(s.getPopularity())
				.withRating(s.getRating())
				.withVisibility(s.getVisibility())
				.withPriority(s.getPriority()).build();
		return toMap;
	};
	
	@Autowired
	public StreamPostServiceImpl(StreamPostRepository streamPostRepository,
			ProfileRepository profileRepository, MessageParser messageParser,
			TagsService tagsService) {
		this.streamPostRepository = streamPostRepository;
		this.profileRepository = profileRepository;
		this.messageParser = messageParser;
		this.tagsService = tagsService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForProfile(int profileId, Version version,
			int start, int count, Sorting sorting, Visibility... levels) {
		
		if (sorting == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Pageable pageRequest = PageRequest.of(start, count);
		Sort sort = Sort.by(sorting.getDirection(), sorting.getDomainProperty());
		
		if (levels == null || levels.length == 0) {
			return streamPostRepository.findAllAssociatedWithProfile(profileId, pageRequest, sort)
					.stream().map(mapper).collect(Collectors.toList());
		}
		
		List<String> levelsValues = 
				Stream.of(levels).map(Visibility::toString).collect(Collectors.toList());
		
		return streamPostRepository.findAllAssociatedWithProfile(profileId, levelsValues, pageRequest, 
				sort).stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForAuthor(int authorId, Version version,
			int start, int count, Sorting sorting, Visibility... levels) {
		
		if (sorting == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Pageable pageRequest = PageRequest.of(start, count);
		Sort sort = Sort.by(sorting.getDirection(), sorting.getDomainProperty());
		
		if (levels == null || levels.length == 0) {
			return streamPostRepository.findAllByAuthorId(authorId, pageRequest, sort)
					.stream().map(mapper).collect(Collectors.toList());
		}
		
		List<String> levelsValues = 
				Stream.of(levels).map(Visibility::toString).collect(Collectors.toList());
		
		return streamPostRepository.findAllByAuthorId(authorId, levelsValues, pageRequest, sort)
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	public Integer createOnProfile(StreamPostDTO post, int profileId,
			Version version) {
		
		if (post == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		
		StreamPost streamPost = new StreamPost();
		streamPost.setAssociatedProfile(this.profileRepository.getOne(profileId));
		streamPost.setAuthorId(post.getAuthorId());
		streamPost.setDate(Instant.now());
		streamPost.setPopularity(0);
		streamPost.setPriority(post.getPriority());
		streamPost.setRating(0);
		streamPost.setVisibility(post.getVisibility());
		
		ParsedMessage pm = this.messageParser.parse(post.getContent());
		
		streamPost.setContent(pm.getParsed());
		
		Integer id  = streamPostRepository.save(streamPost).getId();
		
		tagsService.assosiateWithStreamPostAndAddAllTags(pm.getMessageTags(), id, version);
		
		return id;
	}

	@Override
	public boolean update(StreamPostDTO post, Version version) {
		
		if (post == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		StreamPost streamPost = new StreamPost();
		streamPost.setId(post.getId());
		streamPost.setLastChange(Instant.now());
		streamPost.setPopularity(post.getPopularity());
		streamPost.setPriority(post.getPriority());
		streamPost.setVisibility(post.getVisibility());
		
		ParsedMessage pm = this.messageParser.parse(post.getContent());
		
		streamPost.setContent(pm.getParsed());
		streamPostRepository.save(streamPost);
		
		tagsService.assosiateWithStreamPostAndAddAllTags(pm.getMessageTags(), 
				post.getId(), version);
		return true;
	}

	@Override
	public void deleteStreamPost(int postId) {
		streamPostRepository.deleteById(postId);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean belongsTo(int profileId, int postId) {
		return streamPostRepository.countByAuthorIdAndId(postId, profileId) == 1;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StreamPostDTO> getAllForTag(String tag, Version version,
			int start, int count, Sorting sorting) {
		
		if (sorting == null || version == null || tag == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Pageable pageRequest = PageRequest.of(start, count);
		Sort sort = Sort.by(sorting.getDirection(), sorting.getDomainProperty());
		return streamPostRepository.findAllByTag(tag, pageRequest, sort)
				.stream().map(mapper).collect(Collectors.toList());
	}

}
