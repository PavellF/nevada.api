package org.pavelf.nevada.api.persistence.repository;

/**
 * Defines set of advanced actions with {@code Profile}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface AdvancedProfileReporitory {

	/**
	 * Updates profile rating row based on rating all posted by this user 
	 * messages and posts have. Resource consuming operation. Do not call this 
	 * often.
	 * */
	public void updateRating();
	
	/**
	 * Updates profile popularity based on people that visited this profile.
	 * */
	public void updatePopularity();
}
