package org.pavelf.nevada.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Visibility;

/**
 * General interface for managing images.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PhotoService {

	/**
	 * Relative size of {@code Photo}.
	 * */
	public enum Size {
		SMALL,
		MEDIUM,
		ORIGINAL
	}
	
	/**
	 * Updates given photo.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void update(PhotoDTO photo, Version version);
	
	/**
	 * Fetches raw image bytes.
	 * @param levels image should have at least one given levels. If 
	 * {@code null} passed then image may have any level of visibility.
	 * @return never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public byte[] getRawImageById(int id, Size size, Set<Visibility> levels);
	
	/**
	 * @see #getAllForMessage
	 * */
	public List<PhotoDTO> getAllForStreamPost(int postId, 
			PageAndSortExtended params);
	
	/**
	 * Finds all photos associated with given message.
	 * @return never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<PhotoDTO> getAllForMessage(int messageId, 
			PageAndSortExtended params);
	
	/**
	 * Finds all photos for given profile.
	 * @param levels only specified will be returned, if {@code null} passed
	 * all levels will be returned.
	 * @return never {@code null}.
	 * @throws IllegalArgumentException if params is {@code null}.s
	 * */
	public List<PhotoDTO> getAllForProfile(int profileId, 
			PageAndSortExtended params, Set<Visibility> levels);
	
	/**
	 * Creates new image.
	 * @param photo image to save.
	 * @param raw bytes of object to post.
	 * @return never {@code null} posted object id.
	 * */
	public Integer post(PhotoDTO photo, byte[] raw, Version version);
	
	/**
	 * @param profileId owner id.
	 * @param photoIds photo identifiers, may be {@code null}.
	 * @return Whether all these photos belong profile with given id.
	 * */
	public boolean areBelongsTo(int profileId, Collection<Integer> photoIds);
	
	/**
	 * @param profileId owner id.
	 * @param photoId photo id.
	 * @return Whether this photo belongs profile with given id.
	 * */
	public boolean isBelongsTo(int profileId, int photoIds);
	
}
