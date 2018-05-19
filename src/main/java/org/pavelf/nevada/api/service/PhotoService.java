package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for managing images.
 * @since 1.0
 * @author Pavel F.
 * */
public interface PhotoService {

	/**
	 * Creates new image.
	 * @param photo image to save.
	 * @return never {@code null} mutated PhotoDTO object.
	 * */
	public PhotoDTO post(PhotoDTO photo);
	
}
