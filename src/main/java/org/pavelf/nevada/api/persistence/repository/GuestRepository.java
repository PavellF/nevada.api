package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Guest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestRepository 
	extends JpaRepository<Guest, Integer> {

	@Query("SELECT g FROM Guest g "
			+ "WHERE g.associatedProfile = :profileID "
			+ "AND g.hidden = :hidden")
	public List<Guest> findAllForProfile(@Param("profileID") int profileId,
			@Param("hidden") boolean hiddenOnly, Pageable pageable);
	
}
