package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;

import java.util.Set;

import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for interactions with user profile.
 * @since 1.0
 * @author Pavel F.
 * */
public interface ProfileService {
	
	/**
	 * Creates new user in the application.
	 * @param profile profile which is base for new one.
	 * @param version of object to create.
	 * @return never {@code null}, generated id.
	 * @throws IllegalArgumentException if {@code null} passed.
	 */
	public Integer create(ProfileDTO profile, Version version);
	
	/**
	 * Reads profile from storage by given id.
	 * @param version of object to read.
	 * @param id identifier.
	 * @param hideSensitive whether need to expose sensitive information(e-mails, passwords).
	 * @return read objects or {@link null}.
	 * @throws IllegalArgumentException if version is null.
	 */
	public ProfileDTO read(int id, boolean hideSensitive, Version version);
	
	/**
	 * Reads profiles from storage by given ids.
	 * @param version of objects to read.
	 * @param ids identifiers.
	 * @return read objects, never {@link null} may be empty set.
	 * @throws IllegalArgumentException if null passed.
	 */
	public Set<ProfileDTO> readAll(Set<Integer> ids, Version version);
	
	/**
	 * Updates not null fields of given profile.
	 * @param profile with new fields' values and id field set.
	 * @param version of object to update.
	 * @return never {@code null}, whether update was successful.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public boolean update(ProfileDTO profile, Version version);
	
	/**
	 * Checks whether given password match current profile password.
	 * @param password to compare.
	 * @param profileId compare with this profile password.
	 * @return whether passwords are equal.
	 */
	public boolean arePasswordsEqual(char[] password, int profileId);
	
	/**
	 * Whether profile represented by this id was suspended and suspend time has not been 
	 * elapsed yet.
	 * @param profileId profile id.
	 */
	public boolean isSuspended(int profileId);
	
}
