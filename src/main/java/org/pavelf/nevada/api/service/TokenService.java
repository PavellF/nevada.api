package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.domain.TokenDTO;

/**
 * Represents interactions with security tokens.
 * @since 1.0
 * @author Pavel F. 
 * */
public interface TokenService {

	public TokenDTO create(TokenDTO token);
	
}
