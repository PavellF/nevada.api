package org.pavelf.nevada.api.domain;

/**
 * Describes application object's version.
 * @since 1.0
 * @author Pavel F.
 * */
public interface Version {

	/**
	 * @param version to compare. 
	 * @return If {@code version} is {@code null} then false.
	 * 
	 * */
	public boolean isBelow(Version version);
	
	/**
	 * @param version to compare. 
	 * @return If {@code version} is {@code null} then false.
	 * 
	 * */
	public boolean isAbove(Version version);
	
	/**
	 * @param version to compare. 
	 * @return If {@code version} is {@code null} then false.
	 * 
	 * */
	public boolean isEqual(Version version);
	
	public int getMajor();
	
	public int getMinor();
	
}
