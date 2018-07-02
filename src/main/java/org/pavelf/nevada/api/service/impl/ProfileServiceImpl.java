package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.HttpUtil;
import org.pavelf.nevada.api.HttpUtil.Algorithm;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.ProfilePreferencesDTO.Builder;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.pavelf.nevada.api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code ProfileService}.
 * @since 1.0
 * @author Pavel F.
 * */
@Service
public class ProfileServiceImpl implements ProfileService {

	private ProfileRepository principalRepository;
	private ProfilePreferencesService profilePreferencesService;
	
	private final Builder applicationDefaultProfilePreferences = 
			ProfilePreferencesDTO.builder()
			.withCanPostOnMyStream(Visibility.ME)
			.withPremoderateFollowers(false);
	
	@Autowired
	public ProfileServiceImpl(ProfileRepository principalRepository,
			ProfilePreferencesService profilePreferencesService) {
		this.principalRepository = principalRepository;
		this.profilePreferencesService = profilePreferencesService;
	}

	@Transactional
	@Override
	public Integer create(ProfileDTO profile, Version version) {
		if (profile == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Profile newProfile = new Profile();
		newProfile.setEmail(profile.getEmail());	
		newProfile.setPassword(HttpUtil
				.hash(Algorithm.MD5, profile.getPassword()));
		newProfile.setUsername(profile.getUsername());
		newProfile.setSignDate(Instant.now());
		newProfile.setPopularity(0);
		newProfile.setRating(0);
		
		Integer id = principalRepository.save(newProfile).getId();
		
		profilePreferencesService.create(
				applicationDefaultProfilePreferences.build(id), version);
		
		return id;
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
	@Transactional
	public Set<ProfileDTO> readAll(Set<Integer> ids, Version version) {
		if (ids == null || version == null) {
			throw new IllegalArgumentException("Null passed.");
		}
		
		return principalRepository.findAllById(ids).stream().map((Profile val) -> {
			ProfileDTO.Builder profileDTO = ProfileDTO.builder()
					.withAboutId(val.getAboutId())
					.withId(val.getId())
					.withPictureId(val.getPictureId())
					.withPopularity(val.getPopularity())
					.withRating(val.getRating())
					.withSignDate(val.getSignDate())
					.withUsername(val.getUsername());
					 return profileDTO.build();
			}).collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public boolean update(ProfileDTO profile, Version version) {
		if (profile == null || version == null || profile.getId() == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		final char[] password = profile.getPassword();
		
		final Optional<Profile> maybeProfile = 
				principalRepository.findById(profile.getId());
		
		if (!maybeProfile.isPresent()) {
			return false;
		}
		
		final Profile newProfile = maybeProfile.get();
		
		if (profile.getEmail() != null) {
			newProfile.setEmail(profile.getEmail());
		}
		
		if (profile.getPictureId() != null) {
			newProfile.setPictureId(profile.getPictureId());
		}
		
		if (profile.getAboutId() != null) {
			newProfile.setAboutId(profile.getAboutId());
		}
		
		if (password != null) {
			newProfile.setPassword(HttpUtil.hash(Algorithm.MD5, password));
		}
		
		if (profile.getPopularity() != null) {
			newProfile.setPopularity(profile.getPopularity());
		}
		
		if (profile.getSuspendedUntil() != null) {
			newProfile.setSuspendedUntil(profile.getSuspendedUntil());
		}
		
		if (profile.getUsername() != null) {
			newProfile.setUsername(profile.getUsername());
		}
		
		principalRepository.save(newProfile);
		
		return true;
	}

	@Override
	@Transactional
	public boolean arePasswordsEqual(char[] password, int profileId) {
		if (password == null) {
			return false;
		}
		char[] word = HttpUtil.hash(Algorithm.MD5, password);
		return principalRepository.countIdAndPassword(profileId, word) == 1;
	}

	@Override
	@Transactional
	public boolean isSuspended(int profileId) {
		return principalRepository.isSuspended(profileId, Instant.now()) == 1;
	}

}
