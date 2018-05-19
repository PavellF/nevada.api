package org.pavelf.nevada.api.service;

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
	 * @return never {@code null}, mutated {@code message} object. Some fields may be changed.
	 */
	public MessageDTO post(MessageDTO message);
	
}
