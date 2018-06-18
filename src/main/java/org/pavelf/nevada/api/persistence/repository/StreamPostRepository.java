package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import javax.persistence.SecondaryTable;
import javax.persistence.Tuple;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.service.PageAndSort;
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

	
	@Query(value = "SELECT COUNT(*) FROM StreamPost AS sp"
			+ " WHERE sp.id = ?1 AND sp.authorId = ?2")
	public int countPostBelongAuthor(int id, int authorId);
	
	/**
	 * Finds all posts marked with this tag 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param tag name.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "INNER JOIN StreamPostTag AS spt "
			+ "ON spt.associatedStreamPost = sp.id AND spt.associatedTag = ?1 "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = ?2 "
			+ "AND l.likedStreamPost = sp.id AND sp.visibility = 'ALL'")
	public List<Tuple> findAllByTagWithLikeInfo(
			String tag, int requestingId, Pageable pageable);
	
	/**
	 * Finds all posts ever produced by this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @param visibility only posts that have these will be returned.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} 
	 * or {@code visibility} is {@code null}.
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
	
	/**
	 * Finds all posts ever produced by this user profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId profile identifier.
	 * @param params fetch options.
	 * @param requestingId represents caller.
	 * @return never {@code null}, may be empty {@code List}.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
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
	 * @throws IllegalArgumentException if {@code params} 
	 * or {@code visibility} is {@code null}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id "
			+ "WHERE sp.authorId = :authorId AND sp.visibility IN (:levels)")
	public List<Tuple> findAllByAuthorIdWithLikeInfo(
			@Param("authorId") int authorId, 
			@Param("levels") List<Visibility> visibility, 
			@Param("currentId") int requestingId, Pageable pageable);
	
	/**
	 * Finds all posts where author is given user and 
	 * returns additional {@code Like} field caller rated some posts.
	 * @param authorId author identifier.
	 * @param requestingId represents caller user.
	 * @param params fetch options.
	 * @throws IllegalArgumentException if {@code params} is {@code null}.
	 * */
	@Query("SELECT sp, l FROM StreamPost AS sp "
			+ "LEFT OUTER JOIN Like AS l ON l.likedById = :currentId "
			+ "AND l.likedStreamPost = sp.id WHERE sp.authorId = :authorId")
	public List<Tuple> findAllByAuthorIdWithLikeInfo(
			@Param("authorId") int authorId, 
			@Param("currentId") int requestingId, Pageable pageable);
	
	
	
	
}
