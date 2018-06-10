package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

	//@Query("SELECT COUNT(p) FROM Photo p WHERE id = ?1 AND owner_id = ?2")
	public int countByIdAndOwnerId(int id, int ownerId);
	
}
