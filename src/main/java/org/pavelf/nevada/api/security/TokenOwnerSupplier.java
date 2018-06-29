package org.pavelf.nevada.api.security;


/**
 * Describes methods for obtaining user and token based on request information 
 * (usually headers).
 * @since 1.0
 * @author Pavel F.
 * */
public interface TokenOwnerSupplier {

	/**
	 * Obtains token from somewhere.
	 * @param token current request's token.
	 * @return {@code null} if could not be found or input is null.
	 * */
	public Token fetchToken(String token);
	
}
