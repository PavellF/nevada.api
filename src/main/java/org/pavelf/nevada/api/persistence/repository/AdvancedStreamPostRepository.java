package org.pavelf.nevada.api.persistence.repository;

import java.util.List;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.service.PageAndSort;

/**
 * Defines set of advanced actions with {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedStreamPostRepository {

	/**
	 * Updates rating row based on likes that stream post ever have. 
	 * Resource consuming operation. Do not call this often.
	 * */
	public void updateRating();
	
	
}
