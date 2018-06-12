package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;

/**
 * Defines set of actions for {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostService {

	/**
	 * Finds all posts associated with given profile.
	 * @param profileId that represents profile.
	 * @param version of object to fetch.
	 * @param start describes relative post number from which list should start.
	 * @param count number of objects.
	 * @param sorting filed name and direction of sorting.
	 * @param levels posts with given visibility levels to return. 
	 * If none specified all levels will be returned. 
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForProfile(int profileId, Version version, 
			int start, int count, Sorting sorting, Visibility... levels);
	
	/**
	 * Finds all posts associated with given tag.
	 * @param tag tagname.
	 * @param version of object to fetch.
	 * @param start describes relative post number from which list should start.
	 * @param count number of objects.
	 * @param sorting filed name and direction of sorting.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForTag(String tag, Version version, 
			int start, int count, Sorting sorting);
	
	/**
	 * Finds all posts associated with given author.
	 * @param authorId that represents author.
	 * @param version of object to fetch.
	 * @param start describes relative post number from which list should start.
	 * @param count number of objects.
	 * @param sorting filed name and direction of sorting.
	 * @param levels posts with given visibility levels to return. 
	 * If none specified all levels will be returned. 
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForAuthor(int authorId, Version version,
			int start, int count, Sorting sorting, Visibility... levels);
	
	/**
	 * Creates new post on some profile stream.
	 * @param post itself.
	 * @param profileId id of profile to whom belongs stream.
	 * @param version of object to create.
	 * @return id of newly created {@code StreamPost}. Never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public Integer createOnProfile(StreamPostDTO post, int profileId, Version version);
	
	/**
	 * Updates post.
	 * @param post itself.
	 * @param version of object.
	 * @return Whether object was updated.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public boolean update(StreamPostDTO post, Version version);
	
	/**
	 * Erases {@code StreamPost} from storage.
	 * @param postId to delete.
	 * */
	public void deleteStreamPost(int postId);
	
	/**
	 * Whether this post belongs to this profile.
	 * @param profileId that represents profile.
	 * @param postId to check.
	 * */
	public boolean belongsTo(int profileId, int postId);
}
