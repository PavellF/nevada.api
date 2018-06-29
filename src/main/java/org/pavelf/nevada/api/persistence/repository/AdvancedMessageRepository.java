package org.pavelf.nevada.api.persistence.repository;

/**
 * Defines set of advanced actions with messages.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedMessageRepository {

	/**
	 * Updates rating row based on likes that messages have. 
	 * Resource consuming operation. Do not call this often.
	 * */
	public void updateRating();
	
}
