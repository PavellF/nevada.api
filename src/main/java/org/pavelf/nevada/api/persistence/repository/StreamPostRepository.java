package org.pavelf.nevada.api.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Defines set of actions with external storage of {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostRepository 
	extends JpaRepository<StreamPost, Integer> {

	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "INNER JOIN Follower AS f ON f.followedId=sp.associatedProfile "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?1 "
			+ "AND l.likedStreamPost = sp.id WHERE sp.id IN (?2) "
			+ "AND (sp.visibility = 'ALL' "
			+ "OR (sp.visibility = 'FRIENDS' AND f.followerId = ?1) "
			+ "OR sp.associatedProfile = ?1 OR sp.authorId = ?1)")
	public List<Tuple> findAllSelectedForProfile(int forProfile,
			Iterable<Integer> ids);
	
	@Query("SELECT sp FROM StreamPost AS sp WHERE sp.visibility = 'ALL' "
			+ "AND sp.id IN (?1)")
	public List<StreamPost> findAllSelected(Iterable<Integer> ids);
	
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "INNER JOIN Follower AS f ON f.followedId=sp.associatedProfile "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?1 "
			+ "AND l.likedStreamPost = sp.id WHERE (sp.visibility = 'ALL' "
			+ "OR (sp.visibility = 'FRIENDS' AND f.followerId = ?1) "
			+ "OR sp.associatedProfile = ?1 OR sp.authorId = ?1)")
	public List<Tuple> findAllForProfile(int forProfile, Pageable pageable);
	
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "INNER JOIN Follower AS f ON f.followedId=sp.associatedProfile "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?1 "
			+ "AND l.likedStreamPost = sp.id WHERE sp.date BETWEEN ?1 AND ?2 "
			+ "AND (sp.visibility = 'ALL' "
			+ "OR (sp.visibility = 'FRIENDS' AND f.followerId = ?1) "
			+ "OR sp.associatedProfile = ?1 OR sp.authorId = ?1)")
	public List<Tuple> findAllForProfileForInterval(int forProfile,
			Instant from, Instant until, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp WHERE sp.visibility = 'ALL' "
			+ "AND sp.date BETWEEN ?1 AND ?2")
	public List<StreamPost> findAllVisibleForInterval(
			Instant from, Instant until, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp WHERE sp.visibility = 'ALL'")
	public List<StreamPost> findAllVisible(Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) FROM StreamPost AS sp"
			+ " WHERE sp.id = ?1 AND sp.associatedProfile = ?2")
	public int countProfileHasPost(int id, int profileId);
	
	@Query(value = "SELECT COUNT(*) FROM StreamPost AS sp"
			+ " WHERE sp.id = ?1 AND sp.authorId = ?2")
	public int countPostBelongAuthor(int id, int authorId);
	
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?2 "
			+ "AND l.likedStreamPost = sp.id "
			+ "WHERE sp.visibility IN (?3)")
	public Optional<Tuple> findByIdWithLikeInfo(
			int id, int requestingId, List<Visibility> visibility);
	
	@Query("SELECT sp FROM StreamPost AS sp "
			+ "WHERE sp.visibility IN (?2) AND sp.id = ?1")
	public Optional<StreamPost> findById(int id, List<Visibility> visibility);
	
	/**
	 * Finds all posts marked with this tag 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param tag name.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp INNER JOIN Attachment AS a "
			+ "ON a.toStreamPost = sp.id AND a.tagName = ?1 "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?2 "
			+ "AND l.likedStreamPost = sp.id AND sp.visibility = 'ALL'")
	public List<Tuple> findAllByTagWithLikeInfo(
			String tag, int requestingId, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp INNER JOIN Attachment AS a "
			+ "ON a.toStreamPost = sp.id AND a.tagName = ?1 "
			+ "WHERE sp.visibility = 'ALL'")
	public List<StreamPost> findAllByTag(String tag, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp WHERE sp.associatedProfile = ?1")
	public List<StreamPost> getAllPostsAssociatedWithProfile(
			int profileId, Pageable pageable);
	
	/**
	 * Finds all posts posted on this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @param visibility only posts that have these will be returned.
	 * @return never {@code null}, may be empty {@code List}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id "
			+ "WHERE sp.associatedProfile = :profileId "
			+ "AND sp.visibility IN (:levels)")
	public List<Tuple> getAllPostsAssociatedWithProfileWithLikeInfo(
			@Param("profileId") int profileId, 
			@Param("currentId") int requestingId,
			@Param("levels") List<Visibility> visibility, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp "
			+ "WHERE sp.associatedProfile = :profileId "
			+ "AND sp.visibility IN (:levels)")
	public List<StreamPost> getAllPostsAssociatedWithProfile(
			@Param("profileId") int profileId, 
			@Param("levels") List<Visibility> visibility, Pageable pageable);
	
	/**
	 * Finds all posts posted on this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @return never {@code null}, may be empty {@code List}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id "
			+ "WHERE sp.associatedProfile = :profileId")
	public List<Tuple> getAllPostsAssociatedWithProfileWithLikeInfo(
			@Param("profileId") int profileId, 
			@Param("currentId") int requestingId, Pageable pageable);
	
	/**
	 * Finds all posts where author is given user and 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param authorId author identifier.
	 * @param visibility only posts that have these will be returned.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id "
			+ "WHERE sp.authorId = :authorId AND sp.visibility IN (:levels)")
	public List<Tuple> findAllByAuthorIdWithLikeInfo(
			@Param("authorId") int authorId, 
			@Param("levels") List<Visibility> visibility, 
			@Param("currentId") int requestingId, Pageable pageable);
	
	@Query("SELECT sp FROM StreamPost AS sp "
			+ "WHERE sp.authorId = :authorId AND sp.visibility IN (:levels)")
	public List<StreamPost> findAllByAuthorId(
			@Param("authorId") int authorId, 
			@Param("levels") List<Visibility> visibility, Pageable pageable);
	
	/**
	 * Finds all posts where author is given user and 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param authorId author identifier.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id WHERE sp.authorId = :authorId")
	public List<Tuple> findAllByAuthorIdWithLikeInfo(
			@Param("authorId") int authorId, 
			@Param("currentId") int requestingId, Pageable pageable);
	
}
