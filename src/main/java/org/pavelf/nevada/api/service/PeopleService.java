package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;

/**
 * General interface for interactions with application person object.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PeopleService {

	/**
	 * Registers new person in the application.
	 * @param person person to register.
	 * @param version of object to register.
	 * @return never {@code null}, generated id.
	 * @throws IllegalArgumentException if {@code null} passed. 
	 * */
	public Integer register(PersonDTO person, Version version);
	
	/**
	 * Whether this person associated with given profile.
	 * @param profileId profile id.
	 * @param personId person id.
	 * */
	//public boolean belongsToProfile(int profileId, int personId);
	
	/**
	 * Finds person associated with given profile.
	 * @param profileId that represents profile.
	 * @param version of object to fetch.
	 * @return retrieved person or {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public PersonDTO getForProfile(int profileId, Version version);
	
	/**
	 * Updates given person.
	 * @param person to update.
	 * @param version of object to update.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void update(PersonDTO person, Version version);
}
