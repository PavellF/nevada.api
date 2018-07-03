package org.pavelf.nevada.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;

/**
 * General purpose implementation of {@link #Version}. Immutable.
 * @author Pavel F.
 * @since 1.0
 * */
public final class VersionImpl implements Version {

	private final int major;
	private final int minor;
	private static final Map<String, Version> ALL_VERSIONS = new HashMap<>();
	
	static {
		ALL_VERSIONS.put("1.0", new VersionImpl(1, 0));
	}
	
	private VersionImpl(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}
	
	/**
	 * Constructs object based on {@code String} value.
	 * @param value '1.0' for example.
	 * @return maybe {@code null} if this version does not exist.
	 * */
	public static Version getBy(String value) {
		return (ALL_VERSIONS.containsKey(value)) 
					? ALL_VERSIONS.get(value) 
					: null;
	}
	
	/**
	 * Creates object based on passed version value.
	 * @param version string value (usually derived from header) 
	 * should be two integers delimited by dot, other digits that follows 
	 * these two will be ignored.
	 * @throws WebApplicationException when could not recognize version.
	 * */
	private VersionImpl (String version) {
		String[] splitted = version.split("\\.");
		this.major = Integer.parseInt(splitted[0]);
		this.minor = Integer.parseInt(splitted[1]);
	}

	/**
	 * Constructs object based on header value. 
	 * Takes the first encountered version value.
	 * @param headerValue value with version parameter.
	 * @return version if exists, or {@code null}.
	 * @throws InvalidMediaTypeException when could not parse header.
	 * */
	public static Version valueOf(String headerValue) {
		List<MediaType> mediaTypes = MediaType.parseMediaTypes(headerValue);
			
		for (MediaType mt : mediaTypes) {
			String version = mt.getParameter("version");
			
			if (ALL_VERSIONS.containsKey(version)) {
				return ALL_VERSIONS.get(version);
			}
		}
		
		return null;
	}
	
	public boolean isBelow(Version  version) {
		return (version != null && (major + minor) < 
				(version.getMajor() + version.getMinor()));
	}
	
	public boolean isAbove(Version  version) {
		return (version != null && (major + minor) > 
		(version.getMajor() + version.getMinor()));
	}
	
	public boolean isEqual(Version  version) {
		return (version != null && (major + minor) == 
				(version.getMajor() + version.getMinor()));
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
