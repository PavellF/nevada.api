package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	
	
}
