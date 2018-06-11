package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.QueryDescriptor;
import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.StreamPost;

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
	 * @param descriptor describes fetch options.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForProfile(int profileId, Version version, 
			QueryDescriptor descriptor);
	
	/**
	 * Finds all posts associated with given author.
	 * @param authorId that represents author.
	 * @param version of object to fetch.
	 * @param descriptor describes fetch options.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForAuthor(int authorId, Version version,
			QueryDescriptor descriptor);
	
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
	 * Erases {@code StreamPost} from storage.
	 * @param postId to delete.
	 * @return whether post was deleted.
	 * */
	public boolean deleteStreamPost(int postId);
	
	/**
	 * Whether this post belongs to this profile.
	 * @param profileId that represents profile.
	 * @param postId to check.
	 * */
	public boolean belongsTo(int profileId, int postId);
}
