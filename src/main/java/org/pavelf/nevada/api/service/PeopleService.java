package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for interactions with application person object.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PeopleService {

	/**
	 * Registers new person in the application.
	 * @param person person to register.
	 * @return never {@code null} PersonDTO object. Some fields may be mutated.
	 * */
	public PersonDTO register(PersonDTO person);
	
}
