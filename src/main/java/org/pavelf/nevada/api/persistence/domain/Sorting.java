package org.pavelf.nevada.api.persistence.domain;

import static org.springframework.data.domain.Sort.Direction.*;

import org.springframework.data.domain.Sort.Direction;

/**
 * Enumerates all possible order options. SQL queries will be based on these constants.
 * @author Pavel F.
 * @since 1.0
 * */
public enum Sorting {

	TIME_ASC("id", ASC),
	TIME_DESC("id", DESC),
	POPULARITY_ASC("popularity", ASC),
	POPULARITY_DESC("popularity", DESC),
	RATING_ASC("rating", ASC),
	RATING_DESC("rating", DESC);
	
	private final String domainProperty;
	private final Direction direction;
	
	private Sorting(String domainProperty, Direction direction) {
		this.domainProperty = domainProperty;
		this.direction = direction;
	}

	public String getDomainProperty() {
		return domainProperty;
	}

	public org.springframework.data.domain.Sort.Direction getDirection() {
		return direction;
	}
	
	
	
}
