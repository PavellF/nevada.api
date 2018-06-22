package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import javax.persistence.Tuple;

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
	
	@Query("SELECT m FROM Message AS m WHERE m.associatedStreamPost = ?1")
	public List<Message> findAllUnderPost(int postId, Pageable pageable);
	
	@Query("SELECT m FROM Message AS m "
			+ "WHERE m.associatedStreamPost = ?1 AND m.archived = 0")
	public List<Message> findAllActiveUnderPost(int postId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re) FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "WHERE m.associatedStreamPost = ?1 AND m.replyTo IS NULL")
	public List<Tuple> findAllNotRepliesUnderPost(int postId,Pageable pageable);
	
	@Query("SELECT m, COUNT(re) FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "WHERE m.associatedStreamPost = ?1 "
			+ "AND m.replyTo IS NULL AND m.archived = 0")
	public List<Tuple> findAllActiveNotRepliesUnderPost(
			int postId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re) FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "WHERE m.associatedStreamPost = ?1 AND m.replyTo = ?2")
	public List<Tuple> findAllRepliesUnderPost(int postId, 
			int replyTo, Pageable pageable);
	
	@Query("SELECT m, COUNT(re) FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "WHERE m.associatedStreamPost = ?1 "
			+ "AND m.replyTo = ?2 AND m.archived = 0")
	public List<Tuple> findAllActiveRepliesUnderPost(int postId, 
			int replyTo, Pageable pageable);
	
	@Query("SELECT m FROM Message AS m WHERE m.associatedStreamPost = ?1 "
			+ "AND m.id IN (?2) AND m.archived = 0")
	public List<Message> findAllSelectedUnderPost(int postId, 
			Iterable<Integer> messageIds, Pageable pageable);
	
	@Query("SELECT COUNT(*) FROM Message AS m "
			+ "WHERE m.associatedStreamPost = ?1 AND m.id = ?2")
	public int countPostIdAndMessage(int postId, int messageId);
	
	@Query("SELECT COUNT(*) FROM Message AS m "
			+ "WHERE m.authorId = ?1 AND m.id = ?2")
	public int countMessageIdAndAuthorId(int profileId, int messageId);
	
	@Query("SELECT m.id, m.replyTo FROM Message AS m "
			+ "WHERE m.associatedStreamPost = ?1")
	public List<Tuple> findAllMessageIdReplyIdPairs(int postId);
	
	@Query("SELECT m.id, m.replyTo FROM Message AS m "
			+ "WHERE m.associatedStreamPost = ?1 AND m.archived = 0")
	public List<Tuple> findAllActiveMessageIdReplyIdPairs(int postId);
	
	@Query("SELECT m, l FROM Message AS m LEFT OUTER JOIN Like AS l "
			+ "ON l.likedMessage = m.id AND l.likedById = ?2 "
			+ "WHERE m.authorId = ?1")
	public List<Tuple> findAllForProfileWLike(int profileId, 
			int issuerId, Pageable pageable);
	
	@Query("SELECT m, l FROM Message AS m LEFT OUTER JOIN Like AS l "
			+ "ON l.likedMessage = m.id AND l.likedById = ?2 "
			+ "WHERE m.authorId = ?1 AND m.archived = 0")
	public List<Tuple> findAllActiveForProfileWLike(int profileId, 
			int issuerId, Pageable pageable);
	
	@Query("SELECT m, l FROM Message AS m LEFT OUTER JOIN Like AS l "
			+ "ON l.likedMessage = m.id AND l.likedById = ?2 "
			+ "WHERE m.associatedStreamPost = ?1")
	public List<Tuple> findAllUnderPostWLike(int postId, 
			int issuerId, Pageable pageable);
	
	@Query("SELECT m, l FROM Message AS m LEFT OUTER JOIN Like AS l "
			+ "ON l.likedMessage = m.id AND l.likedById = ?2 "
			+ "WHERE m.associatedStreamPost = ?1 AND m.archived = 0")
	public List<Tuple> findAllActiveUnderPostWLike(int postId, 
			int issuerId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re), l FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "LEFT OUTER JOIN Like AS l ON l.likedMessage = m.id "
			+ "AND l.likedById = ?2 "
			+ "WHERE m.associatedStreamPost = ?1 AND m.replyTo IS NULL")
	public List<Tuple> findAllNotRepliesUnderPostWLike(int postId,
			int issuerId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re), l FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "LEFT OUTER JOIN Like AS l ON l.likedMessage = m.id "
			+ "AND l.likedById = ?2 "
			+ "WHERE m.associatedStreamPost = ?1 "
			+ "AND m.replyTo IS NULL AND m.archived = 0")
	public List<Tuple> findAllActiveNotRepliesUnderPostWLike(
			int postId, int issuerId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re), l FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "LEFT OUTER JOIN Like AS l ON l.likedMessage = m.id "
			+ "AND l.likedById = ?3 "
			+ "WHERE m.associatedStreamPost = ?1 AND m.replyTo = ?2")
	public List<Tuple> findAllRepliesUnderPostWLike(int postId, 
			int replyTo, int issuerId, Pageable pageable);
	
	@Query("SELECT m, COUNT(re), l FROM Message AS m "
			+ "INNER JOIN Message AS re ON re.replyTo = m.id "
			+ "LEFT OUTER JOIN Like AS l ON l.likedMessage = m.id "
			+ "AND l.likedById = ?3 WHERE m.associatedStreamPost = ?1 "
			+ "AND m.replyTo = ?2 AND m.archived = 0")
	public List<Tuple> findAllActiveRepliesUnderPostWLike(int postId, 
			int replyTo, int issuerId, Pageable pageable);
	
	@Query("SELECT m, l FROM Message AS m "
			+ "LEFT OUTER JOIN Like AS l ON l.likedMessage = m.id "
			+ "AND l.likedById = ?3 WHERE m.associatedStreamPost = ?1 "
			+ "AND m.id IN (?2) AND m.archived = 0")
	public List<Tuple> findAllSelectedUnderPostWLike(int postId, 
			Iterable<Integer> messageIds, int issuerId, Pageable pageable);
	
}
