package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Defines set of actions with external storage if {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostRepository 
	extends JpaRepository<StreamPost, Integer> {
	
	@Query(value = "SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, " + 
	"sp.popularity, sp.priority, sp.visibility, sp.commentable, " + 
	"sp.last_change, l.rating, l.id, l.by_user, l.date " + 
	"FROM stream_post AS sp " + 
	"INNER JOIN profile_has_stream_post AS phsp ON phsp.stream_post_id = sp.id " + 
	"INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id " + 
	"LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = :currentUserId " + 
	"WHERE phsp.profile_id = :profileID AND sp.visibility IN (:levels)", nativeQuery = true)
	public List<StreamPost> findAllAssociatedWithProfile(
			@Param("profileID") int profileId, 
			@Param("currentUserId") int currentId,
			@Param("levels") List<Visibility> visibility, Pageable pageable);
	
	
	@Query("SELECT sp FROM StreamPost sp "
			+ "WHERE sp.associatedProfile = :profileID")
	public List<StreamPost> findAllAssociatedWithProfile(
			@Param("profileID")  Profile profileId, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost sp WHERE sp.authorId = :authorId "
			+ "AND sp.visibility IN (:levels)")
	public List<StreamPost> findAllByAuthorId(@Param("authorId") int authorId, 
			@Param("levels") List<String> visibility, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost sp WHERE sp.authorId = :authorId")
	public List<StreamPost> findAllByAuthorId(int authorId, Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) FROM stream_post AS sp"
			+ " WHERE sp.id = ?1 AND sp.author = ?2", nativeQuery = true)
	public int countByAuthorIdAndId(int id, int authorId);
	
	/*@Query(value = "SELECT sp FROM StreamPost sp WHERE sp.associatedTag = ?1 "
			+ "AND sp.visibility = 'ALL'", nativeQuery = false)
	public List<StreamPost> findAllByTag(String tag, Pageable pageable);*/
	
}
