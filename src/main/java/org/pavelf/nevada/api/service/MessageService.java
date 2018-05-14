package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.MessageDTO;

public interface MessageService {

	public MessageDTO post(MessageDTO message);
	
}
