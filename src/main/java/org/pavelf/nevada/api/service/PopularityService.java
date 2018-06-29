package org.pavelf.nevada.api.service;

/**
 * Defines set of methods that help manage entity's popularity expressed in 
 * numeric values.
 * @author Pavel F.
 * @since 1.0
 * */
public interface PopularityService {

	/**
	 * Asynchronously increases popularity of entity identified by this id. If 
	 * there is no entity described by this identifier new entry will be 
	 * created with given rating.
	 * @param identifier of entity.
	 * @param value this value will be added to existing one.
	 * @throws IllegalArgumentException if {@code value} is negative.
	 * */
	public void increaseBy(int identifier, int value);
	
	/**
	 * Asynchronously increases popularity of entity identified by this id 
	 * by one. If there is no entity described by this identifier new entry 
	 * will be created with rating equal to 1.
	 * @param identifier of entity.
	 * */
	public void increase(int identifier);
	
	/**
	 * Zeroes popularity of entity described by given id.
	 * @param identifier of entity.
	 * @return {@code true} if value was changed, {@code false} if there is no 
	 * entity with this identifier 
	 * */
	public boolean clear(int identifier);
	
	/**
	 * Zeroes popularity of all entities.
	 **/
	public void clearAll();
}
