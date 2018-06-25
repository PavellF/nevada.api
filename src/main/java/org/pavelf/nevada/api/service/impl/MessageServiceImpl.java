package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Tuple;

import org.assertj.core.util.Lists;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.service.AttachmentService;
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
	private AttachmentService attachmentService;
	
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
	
	private final Function<Tuple, MessageDTO> wLikeMapper = (Tuple t) -> {
		Message m = t.get(0, Message.class);
		
		MessageDTO.Builder message = MessageDTO.builder()
				.withArchived(m.isArchived())
				.withAuthorId(m.getAuthorId())
				.withContent(m.getContent())
				.withDate(m.getDate())
				.withStreamPostId(m.getAssociatedStreamPost())
				.withId(m.getId())
				.withLastChange(m.getLastChange())
				.withPriority(m.getPriority())
				.withRating(m.getRating())
				.withReplyTo(m.getReplyTo());
		
		Like l = t.get(1, Like.class);
		
		if (l != null) {
			message.withCurrentUserRating(l.getRating());
		} 
		
		addAttachmentsIfAny(message, m.getId());
		
		return message.build();
	};
	
	private final Function<Tuple, MessageDTO> wLikeAndReplies = (Tuple t) -> {
		Message m = t.get(0, Message.class);
		
		MessageDTO.Builder message = MessageDTO.builder()
				.withArchived(m.isArchived())
				.withAuthorId(m.getAuthorId())
				.withContent(m.getContent())
				.withDate(m.getDate())
				.withStreamPostId(m.getAssociatedStreamPost())
				.withId(m.getId())
				.withLastChange(m.getLastChange())
				.withPriority(m.getPriority())
				.withRating(m.getRating())
				.withReplyTo(m.getReplyTo())
				.withCountOfReplies(t.get(1, Integer.class));
		
		Like l = t.get(2, Like.class);
		
		if (l != null) {
			message.withCurrentUserRating(l.getRating());
		} 
		
		addAttachmentsIfAny(message, m.getId());
		
		return message.build();
	};
	
	private final Function<Message, MessageDTO> mapper = (Message m) -> {
		MessageDTO.Builder message = MessageDTO.builder()
				.withArchived(m.isArchived())
				.withAuthorId(m.getAuthorId())
				.withContent(m.getContent())
				.withDate(m.getDate())
				.withStreamPostId(m.getAssociatedStreamPost())
				.withId(m.getId())
				.withLastChange(m.getLastChange())
				.withPriority(m.getPriority())
				.withRating(m.getRating())
				.withReplyTo(m.getReplyTo());
		
		addAttachmentsIfAny(message, m.getId());
		
		return message.build();
	};
	
	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository,
			AttachmentService attachmentService) {
		this.messageRepository = messageRepository;
		this.attachmentService = attachmentService;
	}
	
	protected void addAttachmentsIfAny(MessageDTO.Builder to, int id) {
		Stream<Attachment> attachments = attachmentService
				.getAllMessageAttachments(id).stream();
		
		Set<Integer> photos = attachments.map(Attachment::getPhotoId)
				.collect(Collectors.toSet());
		Set<String> tags = attachments.map(Attachment::getTagName)
				.collect(Collectors.toSet());
		
		to.withImages(photos).withTags(tags);
	}
	
	protected Pageable getPageable(PageAndSort params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),
					params.getCount(), sort);
	}
	
	@Override
	@Transactional
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
		msg.setContent(message.getContent());
		
		int id = messageRepository.save(msg).getId();
		Collection<String> tags = message.getTags();
		Collection<Integer> photos = message.getImages();
		
		attachmentService.assosiateWithMessage(tags, photos, id);
		
		return id;
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
		
		return messages.stream().map(mapper).collect(Collectors.toList());
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
		
		return messages.stream().map(mapper).collect(Collectors.toList());
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
		
		return messages.stream().map(wLikeAndReplies)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> getListOfMessagesUnderStreamPost(int postId,
			PageAndSortExtended params, Iterable<Integer> messageIds) {
		if (params == null || messageIds == null) {
			throw new IllegalArgumentException("Null is disallowed.");
		}
		
		return messageRepository.findAllSelectedUnderPost(
				postId, messageIds, getPageable(params)).stream()
				.map(mapper).collect(Collectors.toList());
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
		
		Integer id = message.getId();
		
		Message toUpdate = new Message();
		toUpdate.setId(id);
		toUpdate.setArchived(message.getArchived());
		toUpdate.setLastChange(Instant.now());
		toUpdate.setPriority(message.getPriority());
		toUpdate.setContent(message.getContent());
		
		Collection<String> tags = message.getTags();
		Collection<Integer> photos = message.getImages();
		attachmentService.updateMessageAttachments(id, tags, photos);
		
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
		
		return messsagesWLike.stream().map(wLikeMapper)
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
		
		return messsagesWLike.stream().map(wLikeMapper)
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
		
		return messages.stream().map(wLikeAndReplies)
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
		
		return messageRepository.findAllSelectedUnderPostWLike(
				postId, messageIds, issuerId, getPageable(params)).stream()
				.map(wLikeMapper).collect(Collectors.toList());
	}

}
