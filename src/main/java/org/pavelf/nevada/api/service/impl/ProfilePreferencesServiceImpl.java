package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.ProfilePreferences;
import org.pavelf.nevada.api.persistence.repository.ProfilePreferencesRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfilePreferencesServiceImpl
		implements ProfilePreferencesService {

	private ProfilePreferencesRepository profilePrefsRepository;
	private ProfileRepository profileRepository;
	
	@Autowired
	public ProfilePreferencesServiceImpl(
			ProfilePreferencesRepository profilePrefsRepository,
			ProfileRepository profileRepository) {
		this.profilePrefsRepository = profilePrefsRepository;
		this.profileRepository = profileRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public ProfilePreferencesDTO getForProfile(
			int profileId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return profilePrefsRepository.findById(profileId)
				.map((ProfilePreferences p) -> {
					ProfilePreferencesDTO prefs = 
							ProfilePreferencesDTO.builder()
							.withCanPostOnMyStream(p.getCanPostOnMyStream())
							.withPremoderateFollowers(
									p.isPremoderateFollowers())
							.build(p.getProfileId());
					return prefs;
		}).orElse(null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer create(ProfilePreferencesDTO prefs, Version version) {
		if (version == null || prefs == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		ProfilePreferences pp = new ProfilePreferences();
		pp.setCanPostOnMyStream(prefs.getCanPostOnMyStream());
		pp.setPremoderateFollowers(prefs.isPremoderateFollowers());
		pp.setProfileId(prefs.getProfileId());
		//pp.setProfile(profileRepository.getOne(prefs.getProfileId()));
		
		pp = profilePrefsRepository.save(pp);
		
		return pp.getProfileId();
	}

	@Override
	@Transactional
	public void update(ProfilePreferencesDTO prefs, Version version) {
		this.create(prefs, version);
	}

}
