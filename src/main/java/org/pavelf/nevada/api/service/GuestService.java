package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.GuestDTO;

/**
 * Interface that defines set of actions for guests. 
 * @author Pavel F.
 * @since 1.0
 * */
public interface GuestService {

	/**
	 * Creates new {@code Guest} object and associates it with profile.
	 * @param profileId profile to visit.
	 * @param hidden will this guest be hidden?
	 * @param who visited id.
	 * @return id of newly created {@code Guest}. Never {@code null}.
	 * */
	public Integer visitUserProfile(int profileId, int who, boolean hidden);
	
	/**
	 * Finds all guests associated with given profile.
	 * @param profileId that represents profile.
	 * @param params parameters of fetched list.
	 * @param includeHidden should hidden guests be fetched or not.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<GuestDTO> getAllGuestsForProfile(
			int profileId, boolean includeHidden, PageAndSortExtended params);
	
}
