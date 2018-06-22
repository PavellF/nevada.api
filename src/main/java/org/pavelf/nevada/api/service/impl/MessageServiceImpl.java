package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Tuple;

import org.assertj.core.util.Lists;
import org.pavelf.nevada.api.domain.Destination;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.ParsedMessage;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.service.MessageParser;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.PageAndSortExtended;
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
		Function<? super Tuple, ? extends MessageDTO>> replyCountMappers;
	
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
		
		replyCountMappers = new HashMap<>();
		replyCountMappers.put(VersionImpl.getBy("1.0"), (Tuple t) -> {
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
			.withReplyTo(m.getReplyTo())
			.withCountOfReplies(t.get(1, Integer.class));
			
			Like l = t.get(2, Like.class);
			
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
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public List<MessageDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		List<Message> messages = (archivedIncluded) 
			? messageRepository.findAllForProfile(profileId, pageable) 
			: messageRepository.findAllActiveForProfile(profileId, pageable);
		
		return messages.stream().map(mappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			PageAndSortExtended params, boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		List<Message> messages = (archivedIncluded) 
			? messageRepository.findAllUnderPost(postId, pageable)
			: messageRepository.findAllActiveUnderPost(postId, pageable);
		
		return messages.stream().map(mappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, Integer messageId,
			boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		List<Tuple> messages;
		final boolean notReplies = messageId == null;
		final boolean replies = !notReplies;
		final Pageable pageable = this.getPageable(params);
		
		if (notReplies && archivedIncluded) {
			messages = messageRepository
					.findAllNotRepliesUnderPost(postId, pageable);
		} else if (notReplies && !archivedIncluded) {
			messages = messageRepository
					.findAllActiveNotRepliesUnderPost(postId, pageable);
		} else if (replies && archivedIncluded) {
			messages = messageRepository
					.findAllRepliesUnderPost(postId, messageId, pageable);
		} else if (replies && !archivedIncluded) {
			messages = messageRepository.findAllActiveRepliesUnderPost(
							postId, messageId, pageable);
		} else {
			messages = Lists.emptyList();
		}
		
		return messages.stream()
				.map(replyCountMappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId,
			PageAndSortExtended params, Iterable<Integer> messageIds) {
		if (params == null || messageIds == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		
		return messageRepository.findAllSelectedUnderPost(
				postId, messageIds, pageable).stream()
				.map(mappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isAuthorOf(int profileId, int messageId) {
		return messageRepository
				.countMessageIdAndAuthorId(profileId, messageId) == 1;
	}

	@Override
	public boolean isPostedUnderPost(int postId, int messageId) {
		return messageRepository.countPostIdAndMessage(postId, messageId) == 1;
	}

	@Override
	@Transactional
	public void update(MessageDTO message, Version version) {
		if (version == null || message == null || message.getId() == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Message toUpdate = new Message();
		toUpdate.setId(message.getId());
		toUpdate.setArchived(message.getArchived());
		toUpdate.setLastChange(Instant.now());
		toUpdate.setPriority(message.getPriority());
		
		ParsedMessage pm = messageParser.parse(message.getContent());
		toUpdate.setContent(pm.getParsed());
		
		messageRepository.save(toUpdate);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Integer, Integer> getMessageIdReplyIdForStreamPost(int postId,
			boolean archivedIncluded) {
		
		List<Tuple> pairs = (archivedIncluded) 
				? messageRepository.findAllMessageIdReplyIdPairs(postId)
				: messageRepository.findAllActiveMessageIdReplyIdPairs(postId);
		
		return pairs.stream().collect(Collectors.toMap(
				t -> t.get(0, Integer.class), t -> t.get(1, Integer.class)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getAllForProfile(int profileId, int issuerId,
			PageAndSortExtended params, boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		Pageable pageable = this.getPageable(params);
		
		List<Tuple> messsagesWLike = (archivedIncluded) 
				? messageRepository
						.findAllForProfileWLike(profileId, issuerId, pageable)
				: messageRepository.findAllActiveForProfileWLike(
						profileId, issuerId, pageable);
		
		return messsagesWLike.stream()
				.map(tupleMappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfRepliesForStreamPost(int postId,
			int issuerId, PageAndSortExtended params,
			boolean archivedIncluded) {
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		List<Tuple> messsagesWLike = (archivedIncluded) 
				? messageRepository
						.findAllUnderPostWLike(postId, issuerId, pageable)
				: messageRepository	.findAllActiveUnderPostWLike(
						postId, issuerId, pageable);
		
		return messsagesWLike.stream()
				.map(tupleMappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfRepliesForMessageUnderStreamPost(
			int postId, PageAndSortExtended params, int issuerId,
			Integer messageId, boolean archivedIncluded) {
		
		if (params == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		List<Tuple> messages;
		final boolean notReplies = messageId == null;
		final boolean replies = !notReplies;
		final Pageable pageable = this.getPageable(params);
		
		if (notReplies && archivedIncluded) {
			messages = messageRepository.findAllNotRepliesUnderPostWLike(
					postId, issuerId, pageable);
		} else if (notReplies && !archivedIncluded) {
			messages = messageRepository.findAllActiveNotRepliesUnderPostWLike(
					postId, issuerId, pageable);
		} else if (replies && archivedIncluded) {
			messages = messageRepository.findAllRepliesUnderPostWLike(
					postId, messageId, issuerId, pageable);
		} else if (replies && !archivedIncluded) {
			messages = messageRepository.findAllActiveRepliesUnderPostWLike(
							postId, messageId, issuerId, pageable);
		} else {
			messages = Lists.emptyList();
		}
		
		return messages.stream()
				.map(replyCountMappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId,
			PageAndSortExtended params, Iterable<Integer> messageIds,
			int issuerId) {
		
		if (params == null || messageIds == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		Pageable pageable = this.getPageable(params);
		
		return messageRepository.findAllSelectedUnderPostWLike(
				postId, messageIds, issuerId, pageable).stream()
				.map(tupleMappers.get(params.getObjectVersion()))
				.collect(Collectors.toList());
	}

	
	
}
