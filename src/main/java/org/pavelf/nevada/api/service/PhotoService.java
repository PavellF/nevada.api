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
	 * @param version of object to post.
	 * @return never {@code null} posted object id.
	 * */
	public Integer post(PhotoDTO photo, Version version);
	
}
