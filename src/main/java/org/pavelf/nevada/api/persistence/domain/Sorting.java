package org.pavelf.nevada.api.persistence.domain;

import static org.springframework.data.domain.Sort.Direction.*;

import org.springframework.data.domain.Sort.Direction;

/**
 * Enumerates all possible order options. SQL queries will be based on these constants.
 * @author Pavel F.
 * @since 1.0
 * */
public enum Sorting {

	TIME_ASC("time", ASC),
	TIME_DESC("time", DESC),
	POPULARITY_ASC("popularity", ASC),
	POPULARITY_DESC("popularity", DESC),
	RATING_ASC("rating", ASC),
	RATING_DESC("rating", DESC);
	
	private final String sortBy;
	private final Direction direction;
	
	public String getSortBy() {
		return sortBy;
	}
	public Direction getDirection() {
		return direction;
	}
	
	private Sorting(String sortBy, Direction direction) {
		this.sortBy = sortBy;
		this.direction = direction;
	}
	
}
