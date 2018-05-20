package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Performs operations with created in-app applications.
 * @since 1.0
 * @author Pavel F.
 * */
public interface ApplicationService {

	/**
	 * Creates new application.
	 * @param application to create.
	 * @param version of object to create.
	 * @return Created application id. Never {@code null}.
	 */
	public Integer create(ApplicationDTO application, Version version);
	
	/**
	 * Gets existing application by id.
	 * @param id identifier,
	 * @param version of object to create.
	 * @return retrieved application.
	 */
	public ApplicationDTO getExisting(int id, Version version);
	
}
