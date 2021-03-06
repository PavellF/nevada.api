package org.pavelf.nevada.api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.pavelf.nevada.api.domain.StreamPostDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.Temporal;

/**
 * Defines set of actions for {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface StreamPostService {

	/**
	 * Finds all posts, returns only those this user has access. Also return 
	 * additional field representing value this user rated post.
	 * @param profileId that represents user. If {@code null} passed only posts
	 * which visible by everyone will be returned.
	 * @param forInterval objects will be fetched from specified relative 
	 * interval until now.
	 * @param params of fetched list.
	 * @return collection of retrieved posts, never {@code null}. Returned 
	 * list may also be empty or less than expected due access restrictions.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllVisibleByProfile(Integer profileId, 
			PageAndSortExtended params, Temporal forInterval);
	
	/**
	 * Finds selected posts, returns only those this user has access. Also 
	 * return additional field representing value this user rated post.
	 * @param profileId that represents user. If {@code null} passed only posts
	 * which visible by everyone will be returned.
	 * @param postIds set of posts ids.
	 * @param version of objects.
	 * @return collection of retrieved posts, never {@code null}. Returned 
	 * list may also be empty or less than expected due access restrictions.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getSelectedVisibleByProfile(Integer profileId, 
			Set<Integer> postIds, Version version);
	
	/**
	 * Finds all posts associated with given profile and 
	 * returns additional field with rating caller rated some posts.
	 * @param profileId that represents profile.
	 * @param pageAndSort parameters of list to fetch.
	 * @param requestingId represents caller who (potentially) liked the post.
	 * @param levels posts with given visibility levels to return. 
	 * 		  If none specified all levels will be returned. 
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForProfile(int profileId,  
			PageAndSortExtended pageAndSort, int requestingId, 
			Visibility... levels);	
	
	/**
	 * Finds all posts associated with given profile.
	 * @param profileId that represents profile.
	 * @param params parameters of list to fetch.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if 
	 *         {@code null} passed except {@code requestingId}.
	 * */
	public List<StreamPostDTO> getAllForProfile(int profileId,  
			PageAndSortExtended params);
	
	/**
	 * Finds all posts associated with given profile.
	 * @param profileId that represents profile.
	 * @param pageAndSort parameters of list to fetch.
	 * @param levels posts with given visibility levels to return.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if  {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForProfile(int profileId,  
			PageAndSortExtended pageAndSort, Visibility... levels);
	
	/**
	 * Finds all posts associated with given tag and 
	 * returns additional field with rating caller rated some messages.
	 * @param tag tagname.
	 * @param version of object.
	 * @param pageAndSort parameters of list to fetch.
	 * @param requestingId requestingId represents caller.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForTag(String tag,
			PageAndSortExtended pageAndSort, int requestingId);
	/**
	 * Finds all posts associated with given tag.
	 * @param tag tagname.
	 * @param pageAndSort parameters of list to fetch.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForTag(String tag, 
			PageAndSortExtended pageAndSort);
	
	/**
	 * Finds all posts associated with given author and 
	 * returns additional field with rating caller rated some messages.
	 * @param authorId that represents author.
	 * @param pageAndSort parameters of list to fetch.
	 * @param requestingId requestingId represents caller.
	 * @param levels posts with given visibility levels to return. 
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if 
	 *         {@code null} passed except {@code requestingId}.
	 * */
	public List<StreamPostDTO> getAllForAuthor(int authorId, 
			int requestingId, PageAndSortExtended pageAndSort, Version version,
			Visibility... levels);
	
	/**
	 * Finds all posts associated with given author.
	 * @param authorId that represents author.
	 * @param pageAndSort parameters of list to fetch.
	 * @param levels posts with given visibility levels to return. 
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<StreamPostDTO> getAllForAuthor(int authorId, Version version,
			PageAndSortExtended pageAndSort, Visibility... levels);
	
	/**
	 * Creates new post on some profile stream.
	 * @param post itself.
	 * @param profileId id of profile to whom belongs stream.
	 * @param version of object to create.
	 * @return id of newly created {@code StreamPost}. Never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public Integer createOnProfile(StreamPostDTO post, 
			int profileId, Version version);
	
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
	 * Whether this post author is this profile.
	 * @param profileId that represents profile.
	 * @param postId to check.
	 * */
	public boolean belongsTo(int profileId, int postId);
	
	/**
	 * Whether this profile owns this post 
	 * (author is not necessary the same person).
	 * @param profileId that represents with owns relationship.
	 * @param postId post identifier.
	 * */
	public boolean profileHasPost(int profileId, int postId);
	
	/**
	 * Obtains stream post by its identifier.
	 * @param postId post identifier.
	 * @param version of object.
	 * @param levels only post with one of these levels 
	 * of visibility will be returned.
	 * @param requestingId represents caller who (potentially) liked the post.
	 * @return post if found or {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public Optional<StreamPostDTO> getById(int postId, Version version,
			int requestingId, Visibility... levels);
	
	/**
	 * Obtains stream post by its identifier.
	 * @param postId post identifier.
	 * @param version of object.
	 * @param levels only post with one of these levels 
	 * of visibility will be returned.
	 * @return post if found or {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public Optional<StreamPostDTO> getById(int postId, Version version,
			Visibility... levels);
}
