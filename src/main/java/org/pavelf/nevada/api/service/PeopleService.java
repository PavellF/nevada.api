package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.PersonDTO;

/**
 * General interface for interactions with person.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PeopleService {

	/**
	 * Registers new person in the application.
	 * @param person person to register.
	 * @return newly created person's id.
	 * */
	public int register(PersonDTO person);
	
}
