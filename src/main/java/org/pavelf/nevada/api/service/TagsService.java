package org.pavelf.nevada.api.service;

import java.util.Set;

import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Service for interactions with user defined tags.
 * @since 1.0
 * @author Pavel F.
 */
public interface TagsService {

	/**
	 * Add tag if not exists.
	 * @param tag tag to add.
	 * @return never {@code null}, mutated {@code tag} object.
	 * */
	public TagDTO addTag(TagDTO tag);
	
	/**
	 * Add tags if not exist.
	 * @param tags tags to add.
	 * @param version of entity to create.
	 * @return never {@code null}, mutated {@code tags}. May be empty set.
	 * */
	public Set<TagDTO> addAllTag(Set<TagDTO> tags);
	
	/**
	 * Adds(if not exist) and associates all given tags with stream post.
	 * @param tags tags to add.
	 * @param version of entity to create.
	 * @param postId to associate.
	 * @return added tags never {@code null}. May be empty set.
	 * */
	public Set<String> assosiateWithStreamPostAndAddAllTags(Set<TagDTO> tags, 
			int postId, Version version);
	
}
