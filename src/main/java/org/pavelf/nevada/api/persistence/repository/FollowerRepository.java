package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.Follower;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowerRepository 
	extends JpaRepository<Follower, Integer> {

	@Query("SELECT COUNT(f) FROM Follower f "
			+ "WHERE (f.followerId = ?1 AND f.followedId = ?2) "
			+ "AND f.mutual = 1")
	public int isFollow(int followerId, int followedId);
	
	@Query("SELECT COUNT(f) FROM Follower f "
			+ "WHERE f.id  = ?2 "
			+ "AND (f.followerId = ?1 OR f.followedId = ?1)")
	public int followerOrFollowed(int profileId, int id);
	
	@Query("SELECT COUNT(f) FROM Follower f "
			+ "WHERE f.followerId = ?1 AND f.followedId = ?2")
	public int hasRelationship(int followerId, int followedId);
	
	@Query("SELECT f FROM Follower f "
			+ "WHERE f.followerId = :profileID AND f.mutual = :mutual")
	public List<Follower> findAllFollowed(@Param("profileID") int profileId,
			@Param("mutual") boolean mutual,
			Pageable pageable, Sort sort);
	
	@Query("SELECT f FROM Follower f "
			+ "WHERE f.followedId = :profileID AND f.mutual = :mutual")
	public List<Follower> findAllFollowers(@Param("profileID") int profileId,
			@Param("mutual") boolean mutual,
			Pageable pageable, Sort sort);
	
}
