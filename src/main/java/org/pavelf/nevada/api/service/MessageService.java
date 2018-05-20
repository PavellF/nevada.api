package org.pavelf.nevada.api.service;

import java.util.List;
import java.util.Set;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for interactions with messages.
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
	public Integer post(MessageDTO message, Version version);
	
	/**
	 * Gets list of messages with given id.
	 * @param ids of messages.
	 * @param version of objects to return.
	 * @return never {@link null} may be empty list.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public List<MessageDTO> getList(Set<Integer> ids, Version version);
	
}
