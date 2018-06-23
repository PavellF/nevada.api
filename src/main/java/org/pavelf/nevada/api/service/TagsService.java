package org.pavelf.nevada.api.service;


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
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void assosiateWithStreamPostAndAddTags(Iterable<String> tags, 
			int postId);
	
	/**
	 * Compares {@code tags} with existing associations and deletes stale 
	 * or saves new ones.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void updateStreamPostTags(int postId, Iterable<String> tags);
	
	/**
	 * Clears all associations between this post and tags.
	 * */
	public void clearAllStreamPostTags(int postId);
}
