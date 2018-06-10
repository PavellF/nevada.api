package org.pavelf.nevada.api.service;

import java.util.List;

import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * Represents interactions with security tokens.
 * @since 1.0
 * @author Pavel F. 
 * */
public interface TokenService {

	/**
	 * Creates token.
	 * @param token token to create.
	 * @param version to create.
	 * @return created token id.
	 * */
	public Integer create(TokenDTO token, Version version);
	
	
	/**
	 * Updates some not null fields of given token.
	 * @param token with new fields' values and id field set.
	 * @param version of object to update.
	 * @return never {@code null}, whether update was successful.
	 * @throws IllegalArgumentException if {@code null} passed.
	 * */
	public boolean update(TokenDTO token, Version version);
	
	/**
	 * Gets all tokens associated with given profile id.
	 * @param profileId profile id.
	 * @param version of object to read.
	 * @return never {@code null} maybe empty {@code List}.
	 * */
	public List<TokenDTO> getAllForProfile(int profileId, Version version);
	
	/**
	 * Gets all tokens issued by application.
	 * @param applicationId application id.
	 * @param version of object to read.
	 * @return never {@code null} maybe empty {@code List}.
	 * */
	public List<TokenDTO> getAllForApplication(int applicationId, Version version);
	
	
	
	
}
