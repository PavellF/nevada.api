package org.pavelf.nevada.api.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Guest is another application user that requests some resource
 * (another user profile, for example).
 * @author Pavel F.
 * @since 1.0
 * */
@JsonInclude(Include.NON_NULL)
public class GuestDTO {

	private Integer id;
	private Integer whoId;
	private Integer toProfile;
	private Boolean hidden;
	private Instant when;

	private GuestDTO(Builder builder) {
		this.id = builder.id;
		this.whoId = builder.whoId;
		this.toProfile = builder.toProfile;
		this.hidden = builder.hidden;
		this.when = builder.when;
	}

	private GuestDTO() { }

	public Integer getId() {
		return id;
	}

	public Integer getWhoId() {
		return whoId;
	}

	public Integer getToProfile() {
		return toProfile;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public Instant getWhen() {
		return when;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GuestDTO [id=");
		builder.append(id);
		builder.append(", whoId=");
		builder.append(whoId);
		builder.append(", toProfile=");
		builder.append(toProfile);
		builder.append(", hidden=");
		builder.append(hidden);
		builder.append(", when=");
		builder.append(when);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link GuestDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link GuestDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer whoId;
		private Integer toProfile;
		private Boolean hidden;
		private Instant when;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withWhoId(Integer whoId) {
			this.whoId = whoId;
			return this;
		}

		public Builder withToProfile(Integer toProfile) {
			this.toProfile = toProfile;
			return this;
		}

		public Builder withHidden(Boolean hidden) {
			this.hidden = hidden;
			return this;
		}

		public Builder withWhen(Instant when) {
			this.when = when;
			return this;
		}

		public GuestDTO build() {
			return new GuestDTO(this);
		}
	}
	
	
}
