package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.criterion.Example;
import org.pavelf.nevada.api.HttpUtil;
import org.pavelf.nevada.api.HttpUtil.Algorithm;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.repository.MessageRepository;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

	private ProfileRepository principalRepository;
	private MessageRepository messageRepository;
	private PhotoRepository photoRepository;
	
	@Autowired
	public ProfileServiceImpl(ProfileRepository principalRepository,
			MessageRepository messageRepository,
			PhotoRepository photoRepository) {
		this.principalRepository = principalRepository;
		this.messageRepository = messageRepository;
		this.photoRepository = photoRepository;
	}

	@Transactional
	@Override
	public Integer create(ProfileDTO profile, Version version) {
		if (profile == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Profile newProfile = new Profile();
		newProfile.setEmail(profile.getEmail());	
		newProfile.setPassword(HttpUtil.hash(Algorithm.MD5, profile.getPassword()).toCharArray());
		newProfile.setUsername(profile.getUsername());
		newProfile.setSignDate(Instant.now());
		newProfile.setPopularity(0);
		newProfile.setRating(0);
		return principalRepository.save(newProfile).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public ProfileDTO read(int id, boolean hideSensitive, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Version is null");
		}
		
		return principalRepository.findById(id).map((Profile val) -> {
			ProfileDTO.Builder profileDTO = ProfileDTO.builder()
					.withAboutId(val.getAboutId())
					.withId(val.getId())
					.withPersonId(val.getId())
					.withPictureId(val.getPictureId())
					.withPopularity(val.getPopularity())
					.withRating(val.getRating())
					.withSignDate(val.getSignDate())
					.withUsername(val.getUsername());
				if (hideSensitive) {
					return profileDTO.build();
				}
				return profileDTO.withEmail(val.getEmail()).build();
			}).orElse(null);
	}

	@Override
	public Set<ProfileDTO> readAll(Set<Integer> ids, Version version) {
		if (ids == null || version == null) {
			throw new IllegalArgumentException("Null passed.");
		}
		
		return principalRepository.findAllById(ids).stream().map((Profile val) -> {
			ProfileDTO.Builder profileDTO = ProfileDTO.builder()
					.withAboutId(val.getAboutId())
					.withId(val.getId())
					.withPersonId(val.getId())
					.withPictureId(val.getPictureId())
					.withPopularity(val.getPopularity())
					.withRating(val.getRating())
					.withSignDate(val.getSignDate())
					.withUsername(val.getUsername());
					 return profileDTO.build();
			}).collect(Collectors.toSet());
	}

	@Override
	public boolean update(ProfileDTO profile, Version version) {
		if (profile == null || version == null || profile.getId() == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Profile newProfile = new Profile();
		newProfile.setId(profile.getId());
		newProfile.setAbout(messageRepository.getOne(profile.getAboutId()));
		newProfile.setEmail(profile.getEmail());
		newProfile.setPersonId(profile.getPersonId());
		newProfile.setPassword(HttpUtil.hash(Algorithm.MD5, profile.getPassword()).toCharArray());
		newProfile.setPicture(photoRepository.getOne(profile.getPictureId()));
		newProfile.setPopularity(profile.getPopularity());
		newProfile.setRating(profile.getRating());
		newProfile.setSuspendedUntil(profile.getSuspendedUntil());
		newProfile.setEmail(profile.getEmail());
		principalRepository.save(newProfile);
		
		return true;
	}

	@Override
	public boolean arePasswordsEqual(char[] password, int profileId) {
		if (password == null) {
			return false;
		}
		return principalRepository.
				countByIdAndPassword(profileId, HttpUtil.hash(Algorithm.MD5, password)) == 1;
	}

	@Override
	public boolean isSuspended(int profileId) {
		return principalRepository.isSuspended(profileId, Instant.now()) == 1;
	}

	

	

	

	
}
