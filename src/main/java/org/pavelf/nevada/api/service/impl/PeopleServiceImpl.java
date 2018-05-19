package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.repository.PeopleRepository;
import org.pavelf.nevada.api.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeopleServiceImpl implements PeopleService {

	private PeopleRepository peopleRepository;
	
	@Autowired
	public PeopleServiceImpl(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Override
	public PersonDTO register(PersonDTO person) {
		Person newPerson = new Person();
		newPerson.setFullName(person.getFullName());
		newPerson.setGender(person.getGender());
		newPerson.setLocation(person.getLocation());
		
		person.setId(peopleRepository.save(newPerson).getId());
		return person;
	}

}
