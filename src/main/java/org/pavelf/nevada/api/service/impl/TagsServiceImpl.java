package org.pavelf.nevada.api.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagsServiceImpl implements TagsService {

	private TagRepository tagRepository;
	
	
	@Override
	@Transactional
	public TagDTO addTag(TagDTO tag) {
		Tag newTag = new Tag();
		newTag.setName(tag.getTagName());
		
		tag.setTagName(tagRepository.save(newTag).getName());
		return tag;
	}

	@Transactional
	@Override
	public Set<TagDTO> addAllTag(Set<TagDTO> tags) {
		List<Tag> saved = tagRepository.saveAll(tags.stream().map((TagDTO tag) -> {
			Tag newTag = new Tag();
			newTag.setName(tag.getTagName());
			return newTag;
		}).collect(Collectors.toSet()));
		
		return tags;
	}

	@Override
	public Set<String> assosiateWithStreamPostAndAddAllTags(Set<TagDTO> tags,
			int postId, Version version) {
		// TODO Auto-generated method stub
		return null;
	}

}
