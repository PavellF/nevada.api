package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.Version;

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
	 * Name of object parameter to use as sorting criteria.
	 * */
	public String getSortingParameter();
	
	/**
	 * Ascending or descending.
	 * */
	public String getSortingDirection();
	
	/**
	 * Version of object
	 * */
	public Version getObjectVersion();
	
}
