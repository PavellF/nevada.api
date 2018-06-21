package org.pavelf.nevada.api.service;

import java.util.List;
import java.util.Map;

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
	 * @param streamPostId id of stream post.
	 * @return never {@code null}, generated identifier.
	 * @throws IllegalArgumentException if null passed.
	 */
	public Integer saveUnderStreamPost(int streamPostId,
			MessageDTO message, Version version);
	
	/**
	 * Finds all messages ever posted by this user.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param archivedIncluded whether to include already archived messages.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * Gets list of messages with given id.
	 * @param postId of post.
	 * @param params parameters of returned list.
	 * @param archivedIncluded whether to include already archived messages.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * Gets list of messages with given id with countOfReplies field populated.
	 * @param postId of post.
	 * @param params parameters of returned list.
	 * @param archivedIncluded whether to include already archived messages.
	 * @param messageId to which return replies. If {@code null} only messages
	 * which are not replies on other messages will be returned.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, 
			Integer messageId, boolean archivedIncluded);	
	
	/**
	 * Gets list of messages with given ids.
	 * @param postId of post.
	 * @param params parameters of returned list.
	 * @param messageIds messages with given ids will be returned.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId, 
			PageAndSortExtended params, Iterable<Integer> messageIds);
	
	/**
	 * @param profileId owner id.
	 * @param messageId message id.
	 * @return Whether this profile is author of message with given id.
	 * */
	public boolean isAuthorOf(int profileId, int messageId);	
	
	/**
	 * @return was this message posted under post identified by given postId.
	 * */
	public boolean isPostedUnderPost(int postId, int messageId); 
	
	/**
	 * Updates message.
	 * @param message message to update.
	 * @param version of object to update.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public void update(MessageDTO message, Version version);
	
	/**
	 * Obtains message id and replied message id pairs, if any. 
	 * @param postId of post.
	 * @param params parameters of returned list.
	 * @param archivedIncluded whether to include already archived messages.
	 * @return never {@link null} may be empty map.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public Map<Integer, Integer> getMessageIdReplyIdForStreamPost(int postId, 
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * Finds all messages ever posted by this user.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param issuerId will be returned additional field with information 
	 * whether this user liked message or {@code null} if not.
	 * @param archivedIncluded whether to include already archived messages.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getAllForProfile(int profileId, int issuerId,
			PageAndSortExtended params, boolean archivedIncluded);
	
	/**
	 * Gets list of messages with given id.
	 * @param postId of post.
	 * @param issuerId will be returned additional field with information 
	 * whether this user liked message or {@code null} if not.
	 * @param params parameters of returned list.
	 * @param archivedIncluded whether to include already archived messages.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			int issuerId, PageAndSortExtended params, 
			boolean archivedIncluded);
	
	/**
	 * Gets list of messages with given id with countOfReplies field populated.
	 * @param postId of post.
	 * @param issuerId will be returned additional field with information 
	 * whether this user liked message or {@code null} if not.
	 * @param params parameters of returned list.
	 * @param archivedIncluded whether to include already archived messages.
	 * @param messageId to which return replies. If {@code null} only messages
	 * which are not replies on other messages will be returned.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, int issuerId,
			Integer messageId, boolean archivedIncluded);	
	
	/**
	 * Gets list of messages with given ids.
	 * @param postId of post.
	 * @param issuerId will be returned additional field with information 
	 * whether this user liked message or {@code null} if not.
	 * @param params parameters of returned list.
	 * @param messageIds messages with given ids will be returned.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId, 
			PageAndSortExtended params, Iterable<Integer> messageIds,
			int issuerId);

}
