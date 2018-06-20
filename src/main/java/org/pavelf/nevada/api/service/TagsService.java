package org.pavelf.nevada.api.service;

import java.util.Set;

import org.pavelf.nevada.api.domain.TagDTO;

/**
 * Service for interactions with user defined tags.
 * @since 1.0
 * @author Pavel F.
 */
public interface TagsService {

	/**
	 * Adds(if not exist) and associates all given tags with stream post.
	 * @param tags tags to add.
	 * @param postId to associate.
	 * @return added tags never {@code null}. May be empty set.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void assosiateWithStreamPostAndAddAllTags(Set<TagDTO> tags, 
			int postId);
	
	/**
	 * Clears all associations between this post and tags.
	 * */
	public void clearAllStreamPostTags(int postId);
	
}
