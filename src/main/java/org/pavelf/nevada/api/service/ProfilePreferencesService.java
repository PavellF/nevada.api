package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Service for manipulating profile's preferences.
 * @author Pavel F.
 * @since 1.0
 * */
public interface ProfilePreferencesService {

	
	public ProfilePreferencesDTO getForProfile(int profileId, Version version);
	
}
