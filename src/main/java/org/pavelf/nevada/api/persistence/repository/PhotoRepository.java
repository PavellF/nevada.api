package org.pavelf.nevada.api.persistence.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.Tuple;

import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

	@Query("SELECT COUNT(p) FROM Photo p "
			+ "WHERE p.id IN (?1) AND p.ownerId = ?2")
	public int countPhotosBelongProfile(
			Iterable<Integer> photoIds, int ownerId);
	
	@Query("SELECT p.small FROM Photo p WHERE p.id = ?1 "
			+ "AND p.visibility IN (?2)")
	public Tuple getSmallRaw(int id, Set<Visibility> levels);
	
	@Query("SELECT p.small FROM Photo p WHERE p.id = ?1")
	public Tuple getSmallRaw(int id);
	
	@Query("SELECT p.medium FROM Photo p WHERE p.id = ?1 "
			+ "AND p.visibility IN (?2)")
	public Tuple getMediumRaw(int id, Set<Visibility> levels);
	
	@Query("SELECT p.medium FROM Photo p WHERE p.id = ?1")
	public Tuple getMediumRaw(int id);
	
	@Query("SELECT p.original FROM Photo p WHERE p.id = ?1 "
			+ "AND p.visibility IN (?2)")
	public Tuple getOriginalRaw(int id, Set<Visibility> levels);
	
	@Query("SELECT p.original FROM Photo p WHERE p.id = ?1")
	public Tuple getOriginalRaw(int id);
	
	@Query("SELECT p FROM Photo p INNER JOIN Attachment AS a "
			+ "ON a.toStreamPost = ?1 AND a.photoId = p.id")
	public List<Photo> getAllForPost(int postId, Pageable pageable);
	
	@Query("SELECT p FROM Photo p INNER JOIN Attachment AS a "
			+ "ON a.toMessage = ?1 AND a.photoId = p.id")
	public List<Photo> getAllForMessage(int messageId, Pageable pageable);
	
	@Query("SELECT p FROM Photo p WHERE p.ownerId = ?1")
	public List<Photo> getAllForOwner(int ownerId, Pageable pageable);
	
	@Query("SELECT p FROM Photo p WHERE p.ownerId = ?1 "
			+ "AND p.visibility IN (?2)")
	public List<Photo> getAllForOwner(int ownerId, Set<Visibility> levels, 
			Pageable pageable);
	
	
}
