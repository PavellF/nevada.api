package org.pavelf.nevada.api.service.impl;


import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Person;
import org.pavelf.nevada.api.persistence.repository.PeopleRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.PeopleService;
import org.springframework.stereotype.Service;

@Service
public class PeopleServiceImpl implements PeopleService {

	private PeopleRepository peopleRepository;
	private ProfileRepository profileRepository;
	
	

	@Override
	public Integer register(PersonDTO person, Version version) {
		if (person == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Person newPerson = new Person();
		newPerson.setProfile(profileRepository.getOne(person.getId()));
		newPerson.setFullName(person.getFullName());
		newPerson.setGender(person.getGender());
		newPerson.setLocation(person.getLocation());
		
		return peopleRepository.save(newPerson).getId();
	}

	@Override
	public PersonDTO getForProfile(int profileId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		return peopleRepository.findById(profileId).map((Person p) -> {
			PersonDTO person = PersonDTO.builder()
					.withFullName(p.getFullName())
					.withGender(p.getGender())
					.withId(p.getId())
					.withLocation(p.getLocation())
					.build();
			return person;
		}).orElse(null);
	}

	@Override
	public void update(PersonDTO person, Version version) {
		this.register(person, version);
	}

}
