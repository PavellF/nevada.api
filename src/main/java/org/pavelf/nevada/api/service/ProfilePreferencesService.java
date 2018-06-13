package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.ProfilePreferencesDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Service for manipulating profile's preferences.
 * @author Pavel F.
 * @since 1.0
 * */
public interface ProfilePreferencesService {

	/**
	 * Persists profile preferences.
	 * @param prefs {@code ProfilePreferences} itself.
	 * @param version of object.
	 * @return id returned by underlying database.
	 * @throws IlegalArgumentException if {@code null} passed.
	 * */
	public Integer create(ProfilePreferencesDTO prefs, Version version);
	
	/**
	 * Gets profile preferences for specified user.
	 * @param profileId profile id.
	 * @param version of object.
	 * @return retrieved object or {@code null}.
	 * @throws IlegalArgumentException if {@code null} passed.
	 * */
	public ProfilePreferencesDTO getForProfile(int profileId, Version version);
	
	/**
	 * Changes and persists profile preferences.
	 * @param prefs {@code ProfilePreferences} itself.
	 * @param version of object.
	 * @throws IlegalArgumentException if {@code null} passed.
	 * */
	public void update(ProfilePreferencesDTO prefs, Version version);
	
}
