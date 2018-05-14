package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.PersonDTO;
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
	public int register(PersonDTO person) {
		// TODO Auto-generated method stub
		return 0;
	}

}
