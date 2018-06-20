package org.pavelf.nevada.api.persistence.repository;

import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code Profile}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	@Query("SELECT COUNT(p) FROM Profile p "
			+ "WHERE p.id = ?1 AND p.password = ?2")
	public int countIdAndPassword(int id, char[] password);
	
	@Query("SELECT COUNT(p) FROM Profile p "
			+ "WHERE p.id = ?1 AND p.suspendedUntil > ?2")
	public int isSuspended(int id, Instant date);
	
}
