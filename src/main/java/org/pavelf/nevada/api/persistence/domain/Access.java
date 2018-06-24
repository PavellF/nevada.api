package org.pavelf.nevada.api.persistence.domain;

/**
 * Defines application specific access restrictions.
 * @since 1.0
 * @author Pavel F.
 * */
public enum Access {

	NONE (0),
	READ (1),
	READ_WRITE (2);
	
	private final int level;

	private Access(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	
	
}
