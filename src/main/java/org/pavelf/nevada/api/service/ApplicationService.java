package org.pavelf.nevada.api.service;

import java.util.Collection;

import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Performs operations with created in-app applications.
 * @since 1.0
 * @author Pavel F.
 * */
public interface ApplicationService {

	/**
	 * Creates new application.
	 * @param application to create.
	 * @param version of object to create.
	 * @return Created application id. Never {@code null}.
	 */
	public Integer create(ApplicationDTO application, Version version);
	
	/**
	 * Gets existing applications that belong to given profile.
	 * @param profileId belongs to profile.
	 * @param version of object to read.
	 * @return collection of retrieved applications. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 */
	public Collection<ApplicationDTO> getAllForProfile(int profileId, Version version);
	
	/**
	 * Updates not null fields of given application.
	 * @param application with new fields' values and id field set.
	 * @param version of object to update.
	 * @return never {@code null}, whether update was successful.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public boolean update(ApplicationDTO application, Version version);
	
	/**
	 * @param profileId owner id.
	 * @param applicationId application id.
	 * @return Whether this application belongs profile with given id.
	 * */
	public boolean isBelongsTo(int profileId, int applicationId);
	
	/**
	 * Whether application represented by this id was suspended and suspend time has not been 
	 * elapsed yet.
	 * @param applicationId application id.
	 */
	public boolean isSuspended(int applicationId);
	
}
