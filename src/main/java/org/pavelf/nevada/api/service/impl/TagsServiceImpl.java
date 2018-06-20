package org.pavelf.nevada.api.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.persistence.domain.StreamPostTag;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.StreamPostTagRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.TagsService;
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
	private StreamPostTagRepository postTagRepository;
	
	
	@Override
	@Transactional
	public void assosiateWithStreamPostAndAddAllTags(Set<TagDTO> tags,
			int postId) {
		if (tags == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		List<Tag> newTags = tags.stream().map((TagDTO t) -> {
			Tag tag = new Tag();
			tag.setName(t.getTagName());
			return tag;
		}).collect(Collectors.toList());
		
		newTags = tagRepository.saveAll(newTags);
		
		List<StreamPostTag> postTags = newTags.stream().map((Tag tag) -> {
			StreamPostTag spt = new StreamPostTag();
			spt.setAssociatedTag(tag.getName());
			spt.setAssociatedStreamPost(postId);
			return spt;
		}).collect(Collectors.toList());
		
		postTagRepository.saveAll(postTags);
	}

	@Override
	@Transactional
	public void clearAllStreamPostTags(int postId) {
		postTagRepository.deleteAllTagsAssociatedWithPost(postId);
		
	}
	
}
