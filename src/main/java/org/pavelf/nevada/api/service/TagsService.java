package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Service for interactions with user defined tags.
 * @since 1.0
 * @author Pavel F.
 */
public interface TagsService {

	/**
	 * Gets all tags for given stream post.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<TagDTO> getAllForStreamPost(int postId, Version version);
	
	/**
	 * Gets all tags for given message.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<TagDTO> getAllForMessage(int messageId, Version version);
	
}
