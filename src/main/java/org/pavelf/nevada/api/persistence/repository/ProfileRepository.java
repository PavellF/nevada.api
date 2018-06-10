package org.pavelf.nevada.api.persistence.repository;

import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	public int countByIdAndPassword(int id, String password);
	
	@Query("SELECT COUNT(p) FROM Profile p WHERE p.id = ?1 AND p.suspendedUntil > ?2")
	public int isSuspended(int id, Instant date);
	
}
