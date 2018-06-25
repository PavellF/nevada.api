package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code Tag}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface TagRepository extends JpaRepository<Tag, String> {

	@Query("SELECT t FROM Tag AS t INNER JOIN Attachment AS a "
			+ "ON a.toStreamPost = ?1 AND a.tagName = t.name")
	public List<Tag> findAllForStreamPost(int postId);
	
	@Query("SELECT t FROM Tag AS t INNER JOIN Attachment AS a "
			+ "ON a.toMessage = ?1 AND a.tagName = t.name")
	public List<Tag> findAllForMessage(int messageId);
}
