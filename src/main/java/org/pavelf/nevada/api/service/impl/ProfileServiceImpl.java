package org.pavelf.nevada.api.service.impl;

import java.time.Instant;

import javax.persistence.EntityNotFoundException;
import javax.persistence.JoinColumn;

import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.PeopleRepository;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.MessageService;
import org.pavelf.nevada.api.service.PeopleService;
import org.pavelf.nevada.api.service.PhotoService;
import org.pavelf.nevada.api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

	private PeopleService peopleService;
	private ProfileRepository principalRepository;
	private PeopleRepository peopleRepository;
	private PhotoRepository photoRepository;
	private MessageRepository messageRepository;
	private MessageService messageService;
	private PhotoService photoService;

	@Autowired
	public ProfileServiceImpl(PeopleService peopleService,
			ProfileRepository principalRepository,
			PeopleRepository peopleRepository, PhotoRepository photoRepository,
			MessageRepository messageRepository, MessageService messageService,
			PhotoService photoService) {
		this.peopleService = peopleService;
		this.principalRepository = principalRepository;
		this.peopleRepository = peopleRepository;
		this.photoRepository = photoRepository;
		this.messageRepository = messageRepository;
		this.messageService = messageService;
		this.photoService = photoService;
	}

	@Transactional
	@Override
	public long create(ProfileDTO profile) {
		int personId = (profile.getPerson() == null) ? 
					peopleService.register(PersonDTO.empty()) : peopleService.register(profile.getPerson());
			
		Profile newProfile = new Profile();
		newProfile.setEmail(profile.getEmail());	
		newProfile.setPassword(profile.getPassword());
		newProfile.setUsername(profile.getUsername());
		newProfile.setSignDate(Instant.now());
		newProfile.setPopularity(0);
		newProfile.setRating(0);
		newProfile.setPerson(this.peopleRepository.getOne(personId));
		
		if (profile.getAbout() != null) {
			Integer id = this.messageService.post(profile.getAbout()).getId();
			newProfile.setAbout(this.messageRepository.getOne(id));
		}
		
		if (profile.getPicture() != null) {
			Integer id = this.photoService.post(profile.getPicture()).getId();
			newProfile.setPicture(this.photoRepository.getOne(id));
		}
		
		return this.principalRepository.save(newProfile).getId();
	}



	

	
}
