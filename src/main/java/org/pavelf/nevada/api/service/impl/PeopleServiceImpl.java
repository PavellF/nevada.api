package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.repository.PeopleRepository;
import org.pavelf.nevada.api.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code PeopleService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class PeopleServiceImpl implements PeopleService {

	private PeopleRepository peopleRepository;
	
	@Autowired
	public PeopleServiceImpl(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@Override
	@Transactional
	public Integer register(PersonDTO person, Version version) {
		if (person == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Person newPerson = new Person();
		newPerson.setAssociatedProfileId(person.getId());
		newPerson.setFullName(person.getFullName());
		newPerson.setGender(person.getGender());
		newPerson.setLocation(person.getLocation());
		
		return peopleRepository.save(newPerson).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public PersonDTO getForProfile(int profileId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		return peopleRepository.findByProfileId(profileId).map((Person p) -> {
			PersonDTO person = PersonDTO.builder()
					.withFullName(p.getFullName())
					.withGender(p.getGender())
					.withId(p.getId())
					.withLocation(p.getLocation())
					.withAssociatedProfileId(p.getAssociatedProfileId())
					.build();
			return person;
		}).orElse(null);
	}

	@Override
	@Transactional
	public void update(PersonDTO person, Version version) {
		if (person == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Person newPerson = new Person();
		newPerson.setId(person.getId());
		newPerson.setAssociatedProfileId(person.getId());
		newPerson.setFullName(person.getFullName());
		newPerson.setGender(person.getGender());
		newPerson.setLocation(person.getLocation());
		
		peopleRepository.save(newPerson);
	}

	@Override
	public boolean belongsToProfile(int profileId, int personId) {
		return peopleRepository
				.countProfileAndPerson(profileId, personId) == 1;
	}

}
