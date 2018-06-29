package org.pavelf.nevada.api.persistence.repository;

/**
 * Defines set of advanced actions with {@code StreamPost}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedStreamPostRepository {

	/**
	 * Updates rating row based on likes that stream post ever had. 
	 * Resource consuming operation. Do not call this often.
	 * */
	public void updateRating();
	
}
