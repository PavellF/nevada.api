package org.pavelf.nevada.api.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents current request's token and optional its owner. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public class Token {

	private final char[] token;
	private final Map<String, String> scopesAccess;
	private final boolean isSuper;
	private final Optional<User> user;
	
	/**
	 * @throws IllegalArcumentException if null passed (except for user).
	 * */
	public Token(char[] token, Map<String, String> scopesAccess, boolean isSuper, User user) {
		if (token == null || scopesAccess == null) {
			throw new IllegalArgumentException();
		}
		this.isSuper = isSuper;
		this.token = token;
		this.scopesAccess =scopesAccess;
		this.user = Optional.ofNullable(user);
	}
	
	/**
	 * @return never {@code null} or throw, may be empty array.
	 * */
	public char[] getToken() {
		return Arrays.copyOf(token, token.length);
	}
	
	/**
	 * Whether at least one scope have access level;
	 * */
	public boolean hasAccess(String acccessLevel, String... scopes) {
		
		if (scopes == null || acccessLevel == null) {
			return false;
		}
		
		for (String scope : scopes) { 
			String tokenScope = scopesAccess.get(scope);
			
			if (tokenScope != null && tokenScope.equals(acccessLevel)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @return whether this token belongs to user with super access.
	 * */
	public boolean isSuper() {
		return isSuper;
	}

	/**
	 * @return current request's user. May be null.
	 * */
	public Optional<User> getUser() {
		return user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Token [token=");
		builder.append(Arrays.toString(token));
		builder.append(", scopesAccess=");
		builder.append(scopesAccess);
		builder.append(", isSuper=");
		builder.append(isSuper);
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSuper ? 1231 : 1237);
		result = prime * result
				+ ((scopesAccess == null) ? 0 : scopesAccess.hashCode());
		result = prime * result + Arrays.hashCode(token);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (isSuper != other.isSuper)
			return false;
		if (scopesAccess == null) {
			if (other.scopesAccess != null)
				return false;
		} else if (!scopesAccess.equals(other.scopesAccess))
			return false;
		if (!Arrays.equals(token, other.token))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	

	
}
