package org.pavelf.nevada.api.service.impl;

import java.util.Optional;

import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.PageAndSortExtended;

/**
 * Basic implementation for {@code PageAndSort}. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public class PageAndSortImpl implements PageAndSortExtended {

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
	public static PageAndSortExtended valueOf(int start, int count, 
			Sorting sorting, Version version) {
		if (sorting == null || version == null) {
			throw new IllegalArgumentException();
		}
		return new PageAndSortImpl(start, count, sorting, version);
	}
	
	/**
	 * Creates new object of type {@code PageAndSort}.
	 * @throws IllegalArgumentException if null passed.
	 * */
	public static PageAndSort valueOf(int start, int count, 
			Sorting sorting) {
		if (sorting == null) {
			throw new IllegalArgumentException();
		}
		return new PageAndSortImpl(start, count, sorting, null);
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
	public Optional<String> getSortBy() {
		return Optional.ofNullable(this.sorting.getSortBy());
	}

	@Override
	public Optional<String> getSortingDirection() {
		return Optional.ofNullable(sorting.getDirection().toString());
	}

	@Override
	public Version getObjectVersion() {
		return version;
	}

}
