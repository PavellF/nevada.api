package org.pavelf.nevada.api.persistence.domain;

/**
 * Defines visibility levels. Less restrictive includes stronger ones. For 
 * instance ALL includes FRIENDS and ME.
 * @author Pavel F.
 * @since 1.0
 * */
public enum Visibility {

	ALL,
	FRIENDS,
	ME
	
}
