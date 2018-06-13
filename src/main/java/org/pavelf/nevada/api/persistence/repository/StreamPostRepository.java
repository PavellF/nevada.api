package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StreamPostRepository extends JpaRepository<StreamPost, Integer> {

	@Query("SELECT sp FROM StreamPost sp "
			+ "WHERE sp.associatedProfile = :profileID "
			+ "AND sp.visibility IN (:levels)")
	public List<StreamPost> findAllAssociatedWithProfile(
			@Param("profileID")  int profileId,
			@Param("levels") List<String> visibility, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost sp "
			+ "WHERE sp.associatedProfile = :profileID")
	public List<StreamPost> findAllAssociatedWithProfile(
			@Param("profileID")  int profileId, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost sp WHERE sp.authorId = :authorId "
			+ "AND sp.visibility IN (:levels)")
	public List<StreamPost> findAllByAuthorId(@Param("authorId") int authorId, 
			@Param("levels") List<String> visibility, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost sp WHERE sp.authorId = :authorId")
	public List<StreamPost> findAllByAuthorId(int authorId, Pageable pageable);
	
	public int countByAuthorIdAndId(int id, int authorId);
	
	@Query("SELECT sp FROM StreamPost sp WHERE sp.associatedTag = ?1 "
			+ "AND sp.visibility = 'ALL'")
	public List<StreamPost> findAllByTag(String tag, Pageable pageable);
	
}
