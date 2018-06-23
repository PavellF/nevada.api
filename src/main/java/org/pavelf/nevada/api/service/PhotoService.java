package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for managing images.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PhotoService {

	/**
	 * Creates new image.
	 * @param photo image to save.
	 * @param version of object to post.
	 * @return never {@code null} posted object id.
	 * */
	public Integer post(PhotoDTO photo, Version version);
	
	/**
	 * Associates all given photos with stream post.
	 * @param ids photos to associate.
	 * @param postId to associate with.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void associateAllWithStreamPost(int postId, Iterable<Integer> ids);
	
	/**
	 * @param profileId owner id.
	 * @param photoIds photo identifiers, may be {@code null}.
	 * @return Whether all these photos belong profile with given id.
	 * */
	public boolean areBelongsTo(int profileId, Iterable<Integer> photoIds);
	
	/**
	 * @param profileId owner id.
	 * @param photoId photo id.
	 * @return Whether this photo belongs profile with given id.
	 * */
	public boolean isBelongsTo(int profileId, int photoIds);
	
	/**
	 * Compares photos with existing associations and deletes stale or saves 
	 * new ones.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void updateStreamPostPhotos(int postId, Iterable<Integer> ids);
	
	/**
	 * Clears all associations between this post and photos.
	 * */
	public void clearAllStreamPostPhotos(int postId);
	
}
