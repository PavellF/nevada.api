package org.pavelf.nevada.api.security;

import java.security.Principal;

/**
 * Represents current request's user. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public class User implements Principal {

	private final String username;
	private final String id;
	private final String email;
	
	/**
	 * @throws IllegalArcumentException if null or empty string passed.
	 * */
	public User(String username, String id, String email) {
		if (username == null || username.isEmpty() || email == null 
				|| email.isEmpty() || id == null || id.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.username = username;
		this.id = id;
		this.email = email;
	}
	
	/**
	 * @return never {@code null} or throw.
	 * */
	@Override
	public String getName() {
		return username;
	}

	/**
	 * @return never {@code null} or throw.
	 * */
	public String getId() {
		return id;
	}

	/**
	 * @return never {@code null} or throw.
	 * */
	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [username=");
		builder.append(username);
		builder.append(", id=");
		builder.append(id);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
}
