package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.service.PageAndSort;

/**
 * Defines set of advanced actions with messages.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedMessageRepository {

	/**
	 * Updates rating row based on likes that messages have. 
	 * Resource consuming operation. Do not call this often.
	 * */
	public void updateRating();
	
	/**
	 * Finds all messages ever produced by this user profile and 
	 * returns additional field with rating that caller rate some messages.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId id of user that liked any 
	 * of these messages (if any).
	 * @param archivedIncluded whether to respect archived messages.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} is null.
	 * */
	public List<MessageDTO> getAllMessagesForProfileWithLikeInfo(int profileId, 
			PageAndSort params,boolean archivedIncluded,Integer requestingId);
	
}
