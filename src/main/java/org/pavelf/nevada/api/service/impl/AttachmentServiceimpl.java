package org.pavelf.nevada.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pavelf.nevada.api.persistence.domain.Attachment;
import org.pavelf.nevada.api.persistence.domain.Tag;
import org.pavelf.nevada.api.persistence.repository.AttachmentRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation of {@code AttachmentService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class AttachmentServiceimpl implements AttachmentService {

	private TagRepository tagRepository;
	private AttachmentRepository attachmentRepository;
	

	public static void main(String...strings) {
		List<String> newOnes = Stream.of("wow","to save")
				.collect(Collectors.toList());
		
		List<String> old = Stream.of("wow", "lol", "toDelete")
				.collect(Collectors.toList());
		
		List<String> toSave = new ArrayList<String>(newOnes.size());
		toSave.addAll(newOnes);
		toSave.removeAll(old);
		
		List<String> toDelete = new ArrayList<String>(old.size());
		toDelete.addAll(old);
		toDelete.removeAll(newOnes);
		
		System.out.println("will be saved " + toSave);
		System.out.println("will be deleted " + toDelete);
	}
	
	
	@Override
	@Transactional
	public void assosiateWithStreamPost(Collection<String> tags,
			Collection<Integer> photoIds, int postId) {
		
		Stream.Builder<Attachment> attachments = Stream.builder();
		
		if (tags != null) {
			Stream<String> tagStream = tags.stream();
			
			Set<Tag> setOfTags = tagStream.map((String name) -> {
				Tag tag = new Tag();
				tag.setName(name);
				return tag;
			}).collect(Collectors.toSet());
			
			tagRepository.saveAll(setOfTags);
			
			tagStream.map((String name) -> {
				Attachment a = new Attachment();
				a.setTagName(name);
				a.setToStreamPost(postId);
				return a;
			}).forEach(attachments::accept);
		}
		
		if (photoIds != null) {
			
			photoIds.stream().map((Integer id) -> {
				Attachment a = new Attachment();
				a.setPhotoId(id);
				a.setToStreamPost(postId);
				return a;
			}).forEach(attachments::accept);
			
		}
		
		attachmentRepository.saveAll(attachments.build()
				.collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public void assosiateWithMessage(Collection<String> tags,
			Collection<Integer> photoIds, int messageId) {
		Stream.Builder<Attachment> attachments = Stream.builder();
		
		if (tags != null) {
			Stream<String> tagStream = tags.stream();
			
			Set<Tag> setOfTags = tagStream.map((String name) -> {
				Tag tag = new Tag();
				tag.setName(name);
				return tag;
			}).collect(Collectors.toSet());
			
			tagRepository.saveAll(setOfTags);
			
			tagStream.map((String name) -> {
				Attachment a = new Attachment();
				a.setTagName(name);
				a.setToMessage(messageId);
				return a;
			}).forEach(attachments::accept);
		}
		
		if (photoIds != null) {
			
			photoIds.stream().map((Integer id) -> {
				Attachment a = new Attachment();
				a.setPhotoId(id);
				a.setToMessage(messageId);
				return a;
			}).forEach(attachments::accept);
			
		}
		
		attachmentRepository.saveAll(attachments.build()
				.collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public void updateStreamPostAttachments(int postId, 
			Collection<String> tags, Collection<Integer> photoIds) {
		
		Stream<Attachment> oldAttachments = attachmentRepository
				.findAllPostAttachments(postId).stream();
		List<String> tagsToSave;
		List<String> tagsToDelete;
		List<Integer> photosToSave;
		List<Integer> photosToDelete;
		
		if (tags != null) {
			Set<String> oldTags = oldAttachments.map(Attachment::getTagName)
					.filter(s -> s != null).collect(Collectors.toSet());
			
			tagsToSave = new ArrayList<>(tags.size());
			tagsToSave.addAll(tags);
			tagsToSave.removeAll(oldTags);
			
			tagsToDelete = new ArrayList<>(oldTags.size());
			tagsToDelete.addAll(oldTags);
			tagsToDelete.removeAll(tags);
			
		}
		
		if (photoIds != null) {
			Set<Integer> oldPhotos = oldAttachments.map(Attachment::getPhotoId)
					.filter(s -> s != null).collect(Collectors.toSet());
			
			photosToSave = new ArrayList<>(photoIds.size());
			photosToSave.addAll(photoIds);
			photosToSave.removeAll(oldPhotos);
			
			photosToDelete = new ArrayList<>(oldPhotos.size());
			photosToDelete.addAll(oldPhotos);
			photosToDelete.removeAll(photoIds);
			
		}
		
		
		/*
		 * 	Set<Attachment> old = attachmentRepository
				.findAllPostAttachments(postId).stream()
				.map((Attachment a) -> {
					return a;
				}).collect(Collectors.toSet());
		List<String> toSave = new ArrayList<String>(tags.size());
		toSave.addAll(tags);
		toSave.removeAll(old);
		
		List<String> toDelete = new ArrayList<String>(old.size());
		toDelete.addAll(old);
		toDelete.removeAll(tags);
		
		assosiateWithStreamPostAndAddTags(toSave, postId);
		attachmentRepository.deleteAllPostTagAttachments(postId, toDelete);*/

	}

	@Override
	public void updateMessageAttachments(int messageId, Collection<String> tags,
			Collection<Integer> photoIds) {
		// TODO Auto-generated method stub

	}

}
