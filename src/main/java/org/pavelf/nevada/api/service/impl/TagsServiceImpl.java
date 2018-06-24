package org.pavelf.nevada.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code TagsService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class TagsServiceImpl implements TagsService {

	private TagRepository tagRepository;
	
	@Autowired
	public TagsServiceImpl(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TagDTO> getAllForStreamPost(int postId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		return tagRepository.findAllForStreamPost(postId).stream()
				.map((Tag t) -> {
					TagDTO tag = new TagDTO();
					tag.setTagName(t.getName());
					return tag;
				}).collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TagDTO> getAllForMessage(int messageId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		return tagRepository.findAllForMessage(messageId).stream()
				.map((Tag t) -> {
					TagDTO tag = new TagDTO();
					tag.setTagName(t.getName());
					return tag;
				}).collect(Collectors.toList());
	}
	
}
