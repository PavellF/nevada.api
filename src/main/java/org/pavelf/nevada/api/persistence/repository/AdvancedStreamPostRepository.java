package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.service.PageAndSort;

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
	 * returns additional field with rating caller rated some messages.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @param archivedIncluded whether to respect archived messages.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} is null.
	 * */
	public List<MessageDTO> getAllPostsForProfileWithLikeInfo(int profileId, 
			PageAndSort params,Integer requestingId);
}
