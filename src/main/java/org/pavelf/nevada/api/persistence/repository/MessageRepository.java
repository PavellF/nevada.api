package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with persistent storage of {@code Message}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface MessageRepository extends JpaRepository<Message, Integer> { 

	@Query("SELECT m FROM Message AS m WHERE m.authorId = ?1")
	public List<Message> findAllForProfile(int profileId, Pageable pageable);
	
	@Query("SELECT m FROM Message AS m "
			+ "WHERE m.authorId = ?1 AND m.archived = 0")
	public List<Message> findAllActiveForProfile(
			int profileId, Pageable pageable);
	
}
