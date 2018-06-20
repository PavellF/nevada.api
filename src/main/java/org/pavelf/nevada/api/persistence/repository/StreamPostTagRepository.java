package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.StreamPostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code StreamPostTag}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostTagRepository
	extends JpaRepository<StreamPostTag, Integer> {

	@Modifying
	@Query("DELETE FROM StreamPostTag spt WHERE spt.associatedStreamPost = ?1")
	public void deleteAllTagsAssociatedWithPost(int streamPostId);
	
}
