package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.LikeDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Defines set of actions with {@code Like}s.
 * @author Pavel F.
 * @since 1.0
 * */
public interface LikeService {

	/**
	 * Finds like object by id.
	 * @param likeId represents like.
	 * @param version of object.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * @return maybe {@code null} or {@code LikeDTO}.
	 * */
	public LikeDTO getById(int likeId, Version version);
	
	/**
	 * Updates given {@code Like}.
	 * @param like to update.
	 * @param version of object.
	 * @throws IllegalArgumentException if {@code null} passed.
	 */
	public void update(LikeDTO like, Version version);
	
	/**
	 * Likes given stream post.
	 * @param like itself.
	 * @param version of object to create.
	 * @param streamPostId id of stream post.
	 * @return never {@code null}, generated identifier.
	 * @throws IllegalArgumentException if null passed.
	 */
	public Integer likePost(LikeDTO like, int streamPostId, Version version);
	
	/**
	 * Likes given message.
	 * @param like itself.
	 * @param version of object to create.
	 * @param messageId id of message.
	 * @return never {@code null}, generated identifier.
	 * @throws IllegalArgumentException if null passed.
	 */
	public Integer likeMessage(LikeDTO like, int messageId, Version version);
	
	/**
	 * Finds all likes ever posted by given profile.
	 * @param profileId profile id.
	 * @param params of returned list.
	 * @return never {@code null}, may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<LikeDTO> getAllForProfile(int profileId, 
			PageAndSortExtended params);
	
	/**
	 * Finds all likes ever posted for given post.
	 * @param postId post id.
	 * @param params of returned list.
	 * @return never {@code null}, may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<LikeDTO> getAllForPost(int postId, PageAndSortExtended params);
	
	/**
	 * Finds all likes ever posted for given message.
	 * @param messageId message id.
	 * @param params of returned list.
	 * @return never {@code null}, may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<LikeDTO> getAllForMessage(int messageId, 
			PageAndSortExtended params);
	
	/**
	 * Whether this post has already been liked by this user.
	 * */
	public boolean isPostAlreadyLikedByUser(int postId, int profileId);
	
	/**
	 * Whether this message has already been liked by this user.
	 * */
	public boolean isMessageAlreadyLikedByUser(int messageId, int profileId);
	
	/**
	 * Whether this like was posted by this user.
	 * */
	public boolean belongsTo(int likeId, int profileId);
	
	/**
	 * Deletes like by given id.
	 * */
	public void delete(int id);
	
}
