package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
