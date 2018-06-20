package org.pavelf.nevada.api.service;

import java.util.List;
import java.util.Set;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for interactions with user messages.
 * @since 1.0
 * @author Pavel F.
 * */
public interface MessageService {

	/**
	 * Creates new message in the application.
	 * @param message message to post.
	 * @param version of object to create.
	 * @return never {@code null}, generated identifier.
	 * @throws IllegalArgumentException if null passed.
	 */
	public Integer saveUnderStreamPost(int streamPostId,
			MessageDTO message, Version version);
	
	
	/**
	 * Finds all messages ever posted by this user.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getAllForProfile(int profileId, 
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * Gets list of messages with given id.
	 * @param ids of messages.
	 * @param version of objects to return.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListForStreamPost(int postId,
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * @param profileId owner id.
	 * @param messageId photo id.
	 * @return Whether this message belongs profile with given id.
	 * */
	public boolean isBelongsTo(int profileId, int messageId);	
	
	public boolean isPostedUnderPost(int postId, int messageId); 
	
	public void update(MessageDTO message, Version version);
	
}
