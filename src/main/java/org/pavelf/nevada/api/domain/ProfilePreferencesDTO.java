package org.pavelf.nevada.api.domain;

import org.pavelf.nevada.api.persistence.domain.Visibility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents user profile preferences.
 * @author Pavel F.
 * @since 1.0
 * */
@JsonInclude(Include.NON_NULL)
public class ProfilePreferencesDTO {

	private Integer id;
	private Integer profileId;
	private Visibility canPostOnMyStream;
	private Boolean premoderateFollowers;

	private ProfilePreferencesDTO(Builder builder, Integer profileId) {
		this.id = builder.id;
		this.profileId = profileId;
		this.canPostOnMyStream = builder.canPostOnMyStream;
		this.premoderateFollowers = builder.premoderateFollowers;
	}

	private ProfilePreferencesDTO() { }

	public Integer getId() {
		return id;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public Visibility getCanPostOnMyStream() {
		return canPostOnMyStream;
	}

	public Boolean getPremoderateFollowers() {
		return premoderateFollowers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfilePreferencesDTO [id=");
		builder.append(id);
		builder.append(", profileId=");
		builder.append(profileId);
		builder.append(", canPostOnMyStream=");
		builder.append(canPostOnMyStream);
		builder.append(", premoderateFollowers=");
		builder.append(premoderateFollowers);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link ProfilePreferencesDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link ProfilePreferencesDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Visibility canPostOnMyStream;
		private Boolean premoderateFollowers;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withCanPostOnMyStream(Visibility canPostOnMyStream) {
			this.canPostOnMyStream = canPostOnMyStream;
			return this;
		}

		public Builder withPremoderateFollowers(Boolean premoderateFollowers) {
			this.premoderateFollowers = premoderateFollowers;
			return this;
		}

		public ProfilePreferencesDTO build(Integer profileId) {
			return new ProfilePreferencesDTO(this, profileId);
		}
	}
	
	
}
