package org.pavelf.nevada.api.domain;

import java.io.Serializable;
import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.annotation.Generated;

/**
 * Represents created application from which will be performed calls on endpoints.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class ApplicationDTO {

	private Integer id;
	private String title;
	private Instant since;
	private String accessKey;
	private Integer profileId;
	private Instant suspendedUntil;

	private ApplicationDTO() { }
	
	private ApplicationDTO(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.since = builder.since;
		this.accessKey = builder.accessKey;
		this.profileId = builder.profileId;
		this.suspendedUntil = builder.suspendedUntil;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApplicationDTO [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", since=");
		builder.append(since);
		builder.append(", accessKey=");
		builder.append(accessKey);
		builder.append(", profileId=");
		builder.append(profileId);
		builder.append(", suspendedUntil=");
		builder.append(suspendedUntil);
		builder.append("]");
		return builder.toString();
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Instant getSince() {
		return since;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public Instant getSuspendedUntil() {
		return suspendedUntil;
	}

	/**
	 * Creates builder to build {@link ApplicationDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link ApplicationDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private String title;
		private Instant since;
		private String accessKey;
		private Integer profileId;
		private Instant suspendedUntil;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withSince(Instant since) {
			this.since = since;
			return this;
		}

		public Builder withAccessKey(String accessKey) {
			this.accessKey = accessKey;
			return this;
		}

		public Builder withProfileId(Integer profileId) {
			this.profileId = profileId;
			return this;
		}

		public Builder withSuspendedUntil(Instant suspendedUntil) {
			this.suspendedUntil = suspendedUntil;
			return this;
		}

		public ApplicationDTO build() {
			return new ApplicationDTO(this);
		}
	}
	
	
}
