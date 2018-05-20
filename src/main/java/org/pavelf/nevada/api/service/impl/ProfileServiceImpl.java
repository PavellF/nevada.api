package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.persistence.JoinColumn;

import org.pavelf.nevada.api.HttpUtil;
import org.pavelf.nevada.api.HttpUtil.Algorithm;
import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Version;
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
	
	@Autowired
	public ProfileServiceImpl(ProfileRepository principalRepository) {
		this.principalRepository = principalRepository;
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

	

	

	

	
}
