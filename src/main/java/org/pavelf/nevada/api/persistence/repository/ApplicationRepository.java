package org.pavelf.nevada.api.persistence.repository;


import java.time.Instant;
import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository 
	extends JpaRepository<Application, Integer> {

	@Query("SELECT a FROM Application a WHERE a.belongsTo = ?1")
	public List<Application> getAllByBelongsTo(int profileId);
	
	@Query("SELECT COUNT(*) FROM Application a "
			+ "WHERE a.belongsTo = ?2 AND a.id = ?1")
	public int countByIdAndBelongsTo(int id, int profileId);
	
	@Query("SELECT COUNT(*) FROM Application p WHERE p.id = ?1 "
			+ "AND p.suspendedUntil > ?2")
	public int isSuspended(int id, Instant date);
	
}
