package org.pavelf.nevada.api.security;

import java.time.Instant;

/**
 * Represents IP address and time when it invoked application. Immutable.
 * @since 1.0
 * @author Pavel F.
 * */
public class LatestIp {

	private final String ip;
	private final Instant lastRequest;
	
	public LatestIp(String ip, Instant lastRequest) {
		this.ip = ip;
		this.lastRequest = lastRequest;
	}

	/**
	 * @return never {@code null} or throw.
	 * */
	public String getIp() {
		return ip;
	}

	/**
	 * @return never {@code null} or throw.
	 * */
	public Instant getLastRequest() {
		return lastRequest;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LatestIp [ip=");
		builder.append(ip);
		builder.append(", lastRequest=");
		builder.append(lastRequest);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result
				+ ((lastRequest == null) ? 0 : lastRequest.hashCode());
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
		LatestIp other = (LatestIp) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (lastRequest == null) {
			if (other.lastRequest != null)
				return false;
		} else if (!lastRequest.equals(other.lastRequest))
			return false;
		return true;
	}
	
}
