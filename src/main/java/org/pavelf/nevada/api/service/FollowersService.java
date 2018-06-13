package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.FollowerDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;

/**
 * General interface for interactions with followers.
 * @author Pavel F.
 * @since 1.0
 * */
public interface FollowersService {

	/**
	 * Finds all profile followers.
	 * @param profileId that represents profile.
	 * @param version of object to fetch.
	 * @param start describes relative object number from which list should start.
	 * @param count number of objects.
	 * @param sorting filed name and direction of sorting.
	 * @param mutualOnly will be fetched.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<FollowerDTO> getAllFollowers(int profileId, Version version, 
			int start, int count, Sorting sorting, boolean mutualOnly);
	
	/**
	 * Finds all profiles that given profile follows.
	 * @param profileId that represents profile.
	 * @param version of object to fetch.
	 * @param start describes relative object number from which list should start.
	 * @param count number of objects.
	 * @param sorting filed name and direction of sorting.
	 * @param mutualOnly will be fetched.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<FollowerDTO> getAllFollowed(int profileId, Version version, 
			int start, int count, Sorting sorting, boolean mutualOnly);
	
	/**
	 * Persist new follower.
	 * @param follower itself.
	 * @param version of object to persist.
	 * @param mutual should or not followed user accept follower.
	 * @return generated identifier. Never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public Integer follow(FollowerDTO follower, Version version, boolean mutual);
	
	/**
	 * Accepts given follower.
	 * @param follower itself.
	 * @param version of object to update.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public void acceptFollower(FollowerDTO follower, Version version);
	
	/**
	 * Whether this user has specified follower(maybe has not accepted yet).
	 * @param followedId user id.
	 * @param followerId his (assumed) follower id.
	 * @return whether this user has specified follower.
	 * @see {@link isFollow()}
	 * */
	public boolean hasRelationship(int followerId, int followedId);
	
	/**
	 * May have sense to call before delete relationship.
	 * @param profileId in relationship as follower or followed.
	 * @param id relationship id.
	 * @return whether this user in relationship as follower or followed user.
	 **/
	public boolean followerOrFollowed(int profileId, int id);
	
	/**
	 * Whether this user has specified already accepted follower.
	 * @param followerId his follower id.
	 * @param followedId user id.
	 * @return whether this user has specified follower.
	 * */
	public boolean isFollow(int followerId, int followedId);
	
	/**
	 * Breaks link between users.
	 * @param id object to delete.
	  */
	public void deleteFollowing(int id);
	
}
