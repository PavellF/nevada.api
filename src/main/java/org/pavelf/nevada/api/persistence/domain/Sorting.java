package org.pavelf.nevada.api.persistence.domain;

/**
 * Enumerates all possible order options. SQL queries will be based on 
 * these constants.
 * @author Pavel F.
 * @since 1.0
 * */
public enum Sorting {

	TIME_ASC,
	TIME_DESC,
	POPULARITY_ASC,
	POPULARITY_DESC,
	RATING_ASC,
	RATING_DESC;
	
}
