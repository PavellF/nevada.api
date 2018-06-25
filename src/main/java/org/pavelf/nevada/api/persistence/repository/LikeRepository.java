package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Defines some basic CRUD methods for {@code Like}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface LikeRepository extends JpaRepository<Like, Integer> {

	@Query("SELECT l FROM Like AS l WHERE l.likedById = ?1")
	public List<Like> findAllForProfile(int profileId, Pageable pageable);
	
	@Query("SELECT l FROM Like AS l WHERE l.likedStreamPost = ?1")
	public List<Like> findAllForPost(int postId, Pageable pageable);
	
	@Query("SELECT l FROM Like AS l WHERE l.likedMessage = ?1")
	public List<Like> findAllForMessage(int messageId, Pageable pageable);
	
	@Query("SELECT COUNT(l) FROM Like AS l WHERE l.likedStreamPost = ?1 "
			+ "AND l.likedById = ?2")
	public int countPostAlreadyLikedByUser(int postId, int profileId);
	
	@Query("SELECT COUNT(l) FROM Like AS l WHERE l.likedMessage = ?1 "
			+ "AND l.likedById = ?2")
	public int countMessageAlreadyLikedByUser(int messageId, int profileId);
	
	@Query("SELECT COUNT(l) FROM Like AS l WHERE l.id = ?1 "
			+ "AND l.likedById = ?2")
	public int countLikedByAndLike(int likeId, int profileId);
	
}
