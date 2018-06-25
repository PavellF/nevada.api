package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.Picture;

/**
 * General purpose image processor.
 * @since 1.0
 * @author Pavel F.
 * */
public interface ImageProcessor {

	/**
	 * Processes given image.
	 * @param toProcess image data.
	 * @throws IllegalArgumentException if null passed.
	 * @return mutated {@code Picture}, never {@code null}.
	 * */
	public Picture process(Picture toProcess);
	
}
