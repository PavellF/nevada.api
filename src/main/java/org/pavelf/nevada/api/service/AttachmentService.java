package org.pavelf.nevada.api.service;

import java.util.Collection;
import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Attachment;

/**
 * Service for linking entities to each other.
 * @since 1.0
 * @author Pavel F.
 */
public interface AttachmentService {

	/**
	 * Deletes all associations given post has.
	 * */
	public void deleteAllAttachmentsOnStreamPost(int postId);
	
	/**
	 * @see #getAllStreamPostAttachments
	 * */
	public List<Attachment> getAllMessageAttachments(int messageId);
	
	/**
	 * Finds all {@code Attachment}s associated with given stream post.
	 * @return Never {@code null}, may be empty list.
	 * */
	public List<Attachment> getAllStreamPostAttachments(int postId);
	
	/**
	 * Adds(if not exist) and associates all given tags and photos with 
	 * stream post. If some of parameters is {@code null} it will be ignored.
	 * @param tags tags to add and associate.
	 * @param photoIds photos to associate.
	 * @param postId to associate with.
	 * */
	public void assosiateWithStreamPost(Collection<String> tags, 
			Collection<Integer> photoIds, int postId);
	
	/**
	 * @see #assosiateWithStreamPost
	 * */
	public void assosiateWithMessage(Collection<String> tags, 
			Collection<Integer> photoIds, int messageId);
	
	/**
	 * Compares {@code Attachment} with existing associations and deletes stale
	 * or saves new ones. If some of parameters is {@code null} it will be 
	 * ignored.
	 * @param tags new set of tags.
	 * @param photoIds new set of photos.
	 * */
	public void updateStreamPostAttachments(int postId, 
			Collection<String> tags, Collection<Integer> photoIds);
	
	/**
	 * @see #updateStreamPostAttachments
	 * */
	public void updateMessageAttachments(int messageId, 
			Collection<String> tags, Collection<Integer> photoIds);
}
