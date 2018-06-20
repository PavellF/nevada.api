package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.ProfilePreferences;
import org.pavelf.nevada.api.persistence.repository
.ProfilePreferencesRepository;
import org.pavelf.nevada.api.service.ProfilePreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code ProfilePreferencesService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class ProfilePreferencesServiceImpl
		implements ProfilePreferencesService {

	private ProfilePreferencesRepository profilePrefsRepository;
	
	@Autowired
	public ProfilePreferencesServiceImpl(
			ProfilePreferencesRepository profilePrefsRepository) {
		this.profilePrefsRepository = profilePrefsRepository;
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
							.withId(p.getId())
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
		pp.setPremoderateFollowers(prefs.getPremoderateFollowers());
		pp.setProfileId(prefs.getProfileId());
		
		pp = profilePrefsRepository.save(pp);
		
		return pp.getId();
	}

	@Override
	@Transactional
	public void update(ProfilePreferencesDTO prefs, Version version) {
		if (version == null || prefs == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		ProfilePreferences pp = new ProfilePreferences();
		pp.setId(prefs.getId());
		pp.setCanPostOnMyStream(prefs.getCanPostOnMyStream());
		pp.setPremoderateFollowers(prefs.getPremoderateFollowers());
		
		profilePrefsRepository.save(pp);
	}

}
