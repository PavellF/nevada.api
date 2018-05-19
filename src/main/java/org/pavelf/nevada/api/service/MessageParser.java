package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.ParsedMessage;

public interface MessageParser {

	public ParsedMessage parse(String message);
	
	
	
}
