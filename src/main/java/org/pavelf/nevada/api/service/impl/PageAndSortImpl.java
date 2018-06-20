package org.pavelf.nevada.api.service.impl;

import java.util.stream.Stream;

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
	private final Stream<Sorting> sorting;
	private final Version version;
	
	private PageAndSortImpl(int start, int count, Stream<Sorting> sorting,
			Version version) {
		this.start = start;
		this.count = count;
		this.sorting = sorting;
		this.version = version;
	}

	/**
	 * Creates new object.
	 * @throws IllegalArgumentException if {@code version} is null.
	 * */
	public static PageAndSortExtended valueOf(int start, int count, 
			Version version, Stream<Sorting> sorting) {
		if (version == null) {
			throw new IllegalArgumentException("Version must not be null.");
		}
		
		Stream<Sorting> sort = (sorting == null) ? 
				Stream.empty() : sorting.distinct();
		
		return new PageAndSortImpl(start, count, sort, version);
	}
	
	/**
	 * Creates new object of type {@code PageAndSort}.
	 * */
	public static PageAndSort valueOf(int start, int count, 
			Stream<Sorting> sorting) {
		
		Stream<Sorting> sort = (sorting == null) ? 
				Stream.empty() : sorting.distinct();
		
		return new PageAndSortImpl(start, count, sort, null);
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
	public Stream<Sorting> getOrderBy() {
		return this.sorting;
	}

	@Override
	public Version getObjectVersion() {
		return version;
	}

}
