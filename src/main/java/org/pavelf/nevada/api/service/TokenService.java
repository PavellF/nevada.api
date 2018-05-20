package org.pavelf.nevada.api.service;

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
	 * Gets token by id.
	 * @param id token id.
	 * @param version to retrieve.
	 * @return Retrieved token. Never {@code null}.
	 * */
	public TokenDTO get(int id, Version version);
	
	/**
	 * Deletes token by id.
	 *  @param id token id.
	 * */
	public void delete(int id);
	
}
