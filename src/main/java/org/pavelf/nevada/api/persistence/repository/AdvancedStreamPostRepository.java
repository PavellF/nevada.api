package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.service.PageAndSort;

/**
 * Defines set of advanced actions with {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedStreamPostRepository {

	/**
	 * Updates rating row based on likes that stream post ever have. 
	 * Resource consuming operation. Do not call this often.
	 * */
	public void updateRating();
	
	/**
	 * Finds all posts ever produced by this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @param visibility only posts that have these will be returned.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} 
	 * or {@code visibility} is {@code null}.
	 * */
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId,int requestingId,
			List<Visibility> visibility, PageAndSort params);
	
	/**
	 * Finds all posts ever produced by this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId, int requestingId, PageAndSort params);
	
	/**
	 * Finds all posts where author is given user and 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param authorId author identifier.
	 * @param visibility only posts that have these will be returned.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} 
	 * or {@code visibility} is {@code null}.
	 * */
	public List<StreamPost> findAllByAuthorIdWithLikeInfo(int authorId, 
			List<Visibility> visibility, int requestingId, PageAndSort params);
	
	/**
	 * Finds all posts where author is given user and 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param authorId author identifier.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	public List<StreamPost> findAllByAuthorIdWithLikeInfo(
			int authorId, int requestingId,PageAndSort params);
	
	/**
	 * Finds all posts marked with this tag 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param tag name.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	public List<StreamPost> findAllByTagWithLikeInfo(String tag,
			int requestingId,PageAndSort params);
}
