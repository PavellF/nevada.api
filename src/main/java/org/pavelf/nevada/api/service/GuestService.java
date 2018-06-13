package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;

/**
 * Interface that defines set of actions for guests. 
 * @author Pavel F.
 * @since 1.0
 * */
public interface GuestService {

	/**
	 * Creates new {@code Guest} object and associates it with profile.
	 * @param profileId profile to associate.
	 * @param hidden will this guest be hidden?
	 * @return id of newly created {@code Guest}. Never {@code null}.
	 * */
	public Integer visitUserProfile(int profileId, boolean hidden);
	
	/**
	 * Finds all guests associated with given profile.
	 * @param profileId that represents profile.
	 * @param version of object to fetch.
	 * @param start describes relative 
	 * object number from which list should start.
	 * @param count number of objects.
	 * @param sorting parameter name and direction of sorting.
	 * @param hiddenOnly should fetch hidden only or not.
	 * @return collection of retrieved posts. May be empty, never {@code null}.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public List<GuestDTO> getAllGuestsForProfile(
			int profileId, Version version, 
			int start, int count, boolean hiddenOnly, Sorting sorting);
	
}
