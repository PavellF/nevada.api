package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

	private MessageRepository messageRepository;
	private MessageParser parser;
	private ProfileRepository profileRepository;
	private TagsService tagsService;
	private TagRepository tagRepository;
	
	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository,
			MessageParser parser, ProfileRepository profileRepository,
			TagsService tagsService, TagRepository tagRepository) {
		this.messageRepository = messageRepository;
		this.parser = parser;
		this.profileRepository = profileRepository;
		this.tagsService = tagsService;
		this.tagRepository = tagRepository;
	}


	@Override
	@Transactional
	public MessageDTO post(MessageDTO message) {
		Message newMessage = new Message();
		newMessage.setAuthor(profileRepository.getOne(message.getAuthorId()));
		newMessage.setArchived(false);
		
		ParsedMessage parsed = parser.parse(message.getContent());
		
		newMessage.setTags(tagsService.addAllTag(parsed.getMessageTags())
				.stream().map(tagDto -> tagRepository.getOne(tagDto.getTagName()))
				.collect(Collectors.toSet()));
		
		newMessage.setContent(parsed.getParsed());
		newMessage.setDate(Instant.now());
		
		Version version = message.getVersion();
		
		if (version.isPrincipalVersion() || version.isSuperVersion()) {
			newMessage.setPriority(message.getPriority());
		} else {
			newMessage.setPriority((short) 0);
		}
		
		newMessage.setRating(0);
		newMessage.setReplyTo(message.getReplyTo());
		message.setId(messageRepository.save(newMessage).getId());
		
		return message;
	}

}
