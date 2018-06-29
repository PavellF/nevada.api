package org.pavelf.nevada.api.security;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Request scoped class that describes request's incoming token and bounded user.
 * Should be substituted with appropriate {@link TokenOwnerSupplier} implementation
 *  in order to obtain actual user and token (usually from external storage).
 * @since 1.0
 * @author Pavel F. 
 * */
@Component
@RequestScope
public class TokenContext {

	protected final Optional<Token> token;
	
	@Autowired
	public TokenContext(TokenOwnerSupplier suppluer, HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		this.token = Optional.ofNullable(suppluer.fetchToken(token));
	}
	
	/**
	 * @return current request's token. May be null, check {@link #TokenContext.isAuthorized()} first.
	 * */
	public Token getToken() {
		return token.orElse(null);
	}

	/**
	 * @return is this request issued with attached token.
	 * */
	public boolean isAuthorized() {
		return token.isPresent();
	}
	
	
}
