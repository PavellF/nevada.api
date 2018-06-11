package org.pavelf.nevada.api.domain;

import static java.util.Collections.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.mockito.internal.util.collections.Sets;
import org.pavelf.nevada.api.persistence.domain.Visibility;

/**
 * Describes additional SQL query parameters like pagination. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public class QueryDescriptor {

	private final Integer start;
	private final Integer quantity;
	private final Set<Visibility> allowedLevels;
	
	public QueryDescriptor(Integer start, Integer quantity, Visibility... allowedLevels) {
		this.start = start;
		this.quantity = quantity;
		this.allowedLevels = (allowedLevels.length == 0) ? emptySet() : Sets.newSet(allowedLevels);
	}

	public Optional<Integer> getStart() {
		return Optional.ofNullable(start);
	}

	public Optional<Integer> getQuantity() {
		return Optional.ofNullable(quantity);
	}

	/**
	 * @return never {@code null}. Maybe empty set.
	 * */
	public Set<Visibility> getAllowedLevels() {
		return allowedLevels;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryDescriptor [start=");
		builder.append(start);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", allowedLevels=");
		builder.append(allowedLevels);
		builder.append("]");
		return builder.toString();
	}

	
	
}
