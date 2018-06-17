package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import javax.persistence.SecondaryTable;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.service.PageAndSort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines set of actions with external storage of {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostRepository 
	extends JpaRepository<StreamPost, Integer> {

	@Query(value = "SELECT COUNT(*) FROM stream_post AS sp"
			+ " WHERE sp.id = ?1 AND sp.author = ?2", nativeQuery = true)
	public int countPostBelongAuthor(int id, int authorId);
	
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "INNER JOIN StreamPostTag AS spht "
			+ "ON spht.associatedStreamPost = sp.id AND spht.associatedTag = ?1 "
			+ "INNER JOIN StreamPostLike AS spl "
			+ "ON spl.associatedStreamPost = sp.id "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?2 "
			+ "AND spl.associatedLike = l.id")
	public List<StreamPost> findAllByTagWithLikeInfo(String tag,
			int requestingId,Pageable pageable);
	
}
