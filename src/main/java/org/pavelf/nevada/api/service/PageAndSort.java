package org.pavelf.nevada.api.service;

import java.util.stream.Stream;

import org.pavelf.nevada.api.persistence.domain.Sorting;

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
	 * Returns stream of {@code Sorting} which contain sorting type property 
	 * names to use as sorting criteria and 
	 * direction (ascending or descending) pairs. Contains no duplicates.
	 * May be empty, never {@code null}.
	 * */
	public Stream<Sorting> getOrderBy();
	
}
