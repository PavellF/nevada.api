package org.pavelf.nevada.api.domain;

import org.pavelf.nevada.api.exception.ExceptionCases;
import org.pavelf.nevada.api.exception.WebApplicationException;
import javax.annotation.Generated;

public final class VersionImpl implements Version {

	private final int major;
	private final int minor;
	
	/**
	 * Creates object based on passed values.
	 * @param version string value (usually derived from header) should be two 
	 * integers delimited by dot, other digits that follows these two will be ignored.
	 * @throws WebApplicationException when could not recognize version.
	 * */
	public VersionImpl (String version) {
		try {
			String[] splitted = version.split("\\.");
			this.major = Integer.parseInt(splitted[1]);
			this.minor = Integer.parseInt(splitted[0]);
			
		} catch (NumberFormatException nfe) {
			throw new WebApplicationException(ExceptionCases.MALFORMED_VERSION);
		}
		
	}
	
	public boolean isBelow(Version  version) {
		return (version != null && (major + minor) < (version.getMajor() + version.getMinor()));
	}
	
	public boolean isAbove(Version  version) {
		return (version != null && (major + minor) > (version.getMajor() + version.getMinor()));
	}
	
	public boolean isEqual(Version  version) {
		return (version != null && (major + minor) == (version.getMajor() + version.getMinor()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Version [major=");
		builder.append(major);
		builder.append(", minor=");
		builder.append(minor);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
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
		VersionImpl  other = (VersionImpl ) obj;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		return true;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

}
