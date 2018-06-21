package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.TagDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code MessageService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class MessageServiceImpl implements MessageService {

	private MessageRepository messageRepository;
	
	private MessageParser messageParser;
	
	private Map<Version, 
		Function<? super Tuple, ? extends MessageDTO>> tupleMappers;
	
	private Map<Version, 
		Function<? super Message, ? extends MessageDTO>> mappers;
	
	private final Function<? super Sorting, ? extends Order> propertyMapper = 
			(Sorting s) -> {
				switch (s) {
				case RATING_ASC: return Order.asc("rating");
				case RATING_DESC: return Order.desc("rating");
				case TIME_ASC: return Order.asc("id");
				case TIME_DESC: return Order.desc("id");
				default: return null;
				}
	};
	
	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository,
			MessageParser messageParser) {
		this.messageRepository = messageRepository;
		this.messageParser = messageParser;
		
		this.tupleMappers = new HashMap<>();
		tupleMappers.put(VersionImpl.getBy("1.0"), (Tuple t) -> {
			Message m = t.get(0, Message.class);
			
			MessageDTO.Builder message = MessageDTO.builder()
					.withArchived(m.isArchived())
					.withAuthorId(m.getAuthorId())
					.withContent(m.getContent())
					.withDate(m.getDate());
			
			Integer underStreamPost = m.getAssociatedStreamPost();
			if (underStreamPost != null) {
				message
				.withDestinationId(underStreamPost)
				.withDestinationType(Destination.STREAM_POST);
			}
			
			message.withId(m.getId())
			.withLastChange(m.getLastChange())
			.withPriority(m.getPriority())
			.withRating(m.getRating())
			.withReplyTo(m.getReplyTo());
			
			Like l = t.get(1, Like.class);
			
			if (l != null) {
				message.withCurrentUserRating(l.getRating());
			} 
			
			return message.build();
		});
		
		this.mappers = new HashMap<>();
		mappers.put(VersionImpl.getBy("1.0"), (Message m) -> {
			MessageDTO.Builder message = MessageDTO.builder()
					.withArchived(m.isArchived())
					.withAuthorId(m.getAuthorId())
					.withContent(m.getContent())
					.withDate(m.getDate());
			
			Integer underStreamPost = m.getAssociatedStreamPost();
			if (underStreamPost != null) {
				message
				.withDestinationId(underStreamPost)
				.withDestinationType(Destination.STREAM_POST);
			}
			
			message.withId(m.getId())
			.withLastChange(m.getLastChange())
			.withPriority(m.getPriority())
			.withRating(m.getRating())
			.withReplyTo(m.getReplyTo())
			.withCurrentUserRating(null);
			
			return message.build();
		});
	}

	protected Pageable getPageable(PageAndSort params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),
					params.getCount(), sort);
	}
	
	@Override
	public Integer saveUnderStreamPost(int streamPostId, MessageDTO message,
			Version version) {
		if (version == null || message == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Message msg = new Message();
		msg.setArchived(false);
		msg.setAssociatedStreamPost(streamPostId);
		msg.setAuthorId(message.getAuthorId());
		msg.setDate(Instant.now());
		msg.setPriority(message.getPriority());
		msg.setRating(0);
		msg.setReplyTo(message.getReplyTo());
		
		ParsedMessage pm = messageParser.parse(message.getContent());
		msg.setContent(pm.getParsed());
		
		return messageRepository.save(msg).getId();
	}
	
	@Override
	public List<MessageDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		List<Message> messages = (archivedIncluded) ? 
				messageRepository.findAllForProfile(profileId, pageable) :
				messageRepository.findAllActiveForProfile(profileId, pageable);
		
		return messages.stream().map(mappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			PageAndSortExtended params, boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, Integer messageId,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId,
			PageAndSortExtended params, Iterable<Integer> messageIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAuthorOf(int profileId, int messageId) {
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

	@Override
	public Map<Integer, Integer> getMessageIdReplyIdForStreamPost(int postId,
			PageAndSortExtended params, boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getAllForProfile(int profileId, int issuerId,
			PageAndSortExtended params, boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			int issuerId, PageAndSortExtended params,
			boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, int issuerId,
			Integer messageId, boolean archivedIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId,
			PageAndSortExtended params, Iterable<Integer> messageIds,
			int issuerId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
