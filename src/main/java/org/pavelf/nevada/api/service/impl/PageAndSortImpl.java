package org.pavelf.nevada.api.service.impl;

import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.service.PageAndSort;

/**
 * Basic implementation for {@code PageAndSort}. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public class PageAndSortImpl implements PageAndSort {

	private final int start;
	private final int count;
	private final Sorting sorting;
	private final Version version;
	
	private PageAndSortImpl(int start, int count, Sorting sorting,
			Version version) {
		this.start = start;
		this.count = count;
		this.sorting = sorting;
		this.version = version;
	}

	/**
	 * Creates new object.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public static PageAndSortImpl valueOf(int start, int count, 
			Sorting sorting, Version version) {
		if (sorting == null || version == null) {
			throw new IllegalArgumentException();
		}
		return new PageAndSortImpl(start, count, sorting, version);
	}
	
	@Override
	public int getStartIndex() {
		return start;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getSortingParameter() {
		return this.getSortingParameter();
	}

	@Override
	public String getSortingDirection() {
		return this.sorting.getDirection().toString();
	}

	@Override
	public Version getObjectVersion() {
		return version;
	}

}
