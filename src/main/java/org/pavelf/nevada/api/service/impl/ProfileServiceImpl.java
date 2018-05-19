package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.EntityNotFoundException;
import javax.persistence.JoinColumn;

import org.pavelf.nevada.api.domain.MessageDTO;
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

	private ProfileRepository principalRepository;
	private PeopleRepository peopleRepository;
	private PhotoRepository photoRepository;
	private MessageRepository messageRepository;
	
	@Autowired
	public ProfileServiceImpl(ProfileRepository principalRepository,
			PeopleRepository peopleRepository, PhotoRepository photoRepository,
			MessageRepository messageRepository) {
		this.principalRepository = principalRepository;
		this.peopleRepository = peopleRepository;
		this.photoRepository = photoRepository;
		this.messageRepository = messageRepository;
	}

	@Transactional
	@Override
	public ProfileDTO create(ProfileDTO profile) {
		if (profile == null) {
			throw new IllegalArgumentException();
		}
		
		Profile newProfile = new Profile();
		newProfile.setEmail(profile.getEmail());	
		newProfile.setPassword(profile.getPassword());
		newProfile.setUsername(profile.getUsername());
		newProfile.setSignDate(Instant.now());
		newProfile.setPopularity(0);
		newProfile.setRating(0);
		newProfile.setPerson(peopleRepository.getOne(profile.getPersonId()));
		profile.setId(principalRepository.save(newProfile).getId());
		return profile;
	}

	

	

	

	
}
