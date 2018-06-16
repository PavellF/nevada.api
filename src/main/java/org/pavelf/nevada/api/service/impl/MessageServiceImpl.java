package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageServiceImpl implements MessageService {

	@Override
	public Integer saveUnderStreamPost(int streamPostId, MessageDTO message,
			Version version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getAllForProfile(int profileId, PageAndSort params,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListForStreamPost(int postId, PageAndSort params,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBelongsTo(int profileId, int messageId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPostedUnderPost(int postId, int messageId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(MessageDTO message, Version version) {
		// TODO Auto-generated method stub
		
	}

	/*private MessageRepository messageRepository;
	private ProfileRepository profileRepository;
	private PhotoRepository photoRepository;
	private TagRepository tagRepository;
	private MessageParser parser;
	
	//@Override
	@Transactional
	public Integer post(MessageDTO message, Version version) {
		if (message == null || version == null) {
			throw new IllegalArgumentException();
		}
		
		Message newMessage = new Message();
		newMessage.setAuthor(profileRepository.getOne(message.getAuthorId()));
		newMessage.setArchived(false);
		
		ParsedMessage parsed = parser.parse(message.getContent());
		
		newMessage.setTags(parsed.getMessageTags().stream()
				.map(tagDto -> tagRepository.getOne(tagDto.getTagName()))
				.collect(Collectors.toSet()));
		
		
		newMessage.setContent(parsed.getParsed());
		newMessage.setDate(Instant.now());
		newMessage.setPriority(message.getPriority());
		newMessage.setRating(0);
		newMessage.setReplyTo(message.getReplyTo());
		
		if (message.getPhotoIds() != null) {
			Set<Photo> photos = message.getPhotoIds().stream()
					.map(photoRepository::getOne).collect(Collectors.toSet());
			newMessage.setPhotos(photos);
		}
		
		return messageRepository.save(newMessage).getId();
	}

	//@Override
	public List<MessageDTO> getList(Set<Integer> ids, Version version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBelongsTo(int profileId, int messageId) {
		return messageRepository.countByIdAndAuthorId(messageId, profileId) == 1;
	}

	@Override
	public Integer saveUnderStreamPost(int streamPostId, MessageDTO message,
			Version version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getAllForProfile(int profileId, PageAndSort params,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListForStreamPost(int postId, PageAndSort params,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPostedUnderPost(int postId, int messageId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(MessageDTO message, Version version) {
		// TODO Auto-generated method stub
		
	}
*/
}
