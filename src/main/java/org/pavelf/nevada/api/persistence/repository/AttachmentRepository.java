package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code Attachment}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AttachmentRepository
	extends JpaRepository<Attachment, Integer> {

	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toStreamPost = ?1")
	public void deleteAllPostAttachments(int streamPostId);
	
	@Query("SELECT a FROM Attachment a WHERE a.toStreamPost = ?1")
	public List<Attachment> findAllPostAttachments(int streamPostId);
	
	@Query("SELECT a FROM Attachment a WHERE a.toMessage = ?1")
	public List<Attachment> findAllMessageAttachments(int messageId);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toStreamPost = ?1 "
			+ "AND (a.tagName IN (?2) OR a.photoId IN (?3))")
	public void deleteAllPostAttachmentsIn(int postId, 
			Iterable<String> tagsToDelete, List<Integer> photosToDelete);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toStreamPost = ?1 "
			+ "AND a.tagName IN (?2)")
	public void deleteAllPostAttachmentsIn(int postId, 
			Iterable<String> tagsToDelete);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toStreamPost = ?1 "
			+ "AND a.photoId IN (?2)")
	public void deleteAllPostAttachmentsIn(int postId, 
			List<Integer> photosToDelete);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toMessage = ?1 "
			+ "AND (a.tagName IN (?2) OR a.photoId IN (?3))")
	public void deleteAllMessageAttachmentsIn(int messageId, 
			Iterable<String> tagsToDelete, List<Integer> photosToDelete);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toMessage = ?1 "
			+ "AND a.tagName IN (?2)")
	public void deleteAllMessageAttachmentsIn(int messageId, 
			Iterable<String> tagsToDelete);
	
	@Modifying
	@Query("DELETE FROM Attachment a WHERE a.toMessage = ?1 "
			+ "AND a.photoId IN (?2)")
	public void deleteAllMessageAttachmentsIn(int messageId, 
			List<Integer> photosToDelete);
	
}
