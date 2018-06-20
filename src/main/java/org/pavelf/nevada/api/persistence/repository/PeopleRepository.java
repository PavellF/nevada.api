package org.pavelf.nevada.api.persistence.repository;

import java.util.Optional;

import org.pavelf.nevada.api.persistence.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code Person}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface PeopleRepository 
	extends JpaRepository<Person, Integer> {

	@Query("SELECT p FROM Person AS p WHERE p.associatedProfileId = ?1")
	public Optional<Person> findByProfileId(int profileId);
	
	@Query("SELECT COUNT(*) FROM Person AS p "
			+ "WHERE p.associatedProfileId = ?1 AND p.id = ?2")
	public int countProfileAndPerson(int profileId, int personId);
	
}
