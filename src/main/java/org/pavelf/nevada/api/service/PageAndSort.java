package org.pavelf.nevada.api.service;

import java.util.Optional;

/**
 * Encapsulates fetch options for list of objects.
 * @author Pavel F.
 * @since 1.0
 * */
public interface PageAndSort {

	/**
	 * From which list should start.
	 * @return start index.
	 * */
	public int getStartIndex();
	
	/**
	 * @return number of objects. Actual count of object that will be 
	 * returned may differ.
	 * */
	public int getCount();
	
	/**
	 * Sorting type name to use as sorting criteria.
	 * */
	public Optional<String> getSortBy();
	
	/**
	 * Ascending or descending.
	 * */
	public Optional<String> getSortingDirection();
	
	
}
