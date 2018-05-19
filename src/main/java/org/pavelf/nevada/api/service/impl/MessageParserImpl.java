package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.service.MessageParser;
import org.springframework.stereotype.Service;

@Service
public class MessageParserImpl implements MessageParser {

	@Override
	public ParsedMessage parse(String message) {
		
		return new ParsedMessage();
	}

}
