package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.service.PageAndSort;
import org.springframework.data.domain.Pageable;

/**
 * Defines set of advanced actions with stream posts.
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
	 * @param visibility only posts that have these will be returned 
	 * if {@code null} then ignored.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} 
	 * and {@code visibility} is {@code null}.
	 * */
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId,int requestingId,
			List<Visibility> visibility, Pageable params);
	
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
			int profileId, int requestingId, Pageable params);
}
