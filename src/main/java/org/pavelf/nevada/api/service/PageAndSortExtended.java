package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.Version;

/**
 * Extended version of {@code PageAndSort} that includes {@code Version}.
 * @author Pavel F.
 * @since 1.0
 */
public interface PageAndSortExtended extends PageAndSort {

	/**
	 * Version of object
	 * */
	public Version getObjectVersion();
}
