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

	private Integer profileId;
	private Visibility canPostOnMyStream;
	private Boolean premoderateFollowers;

	private ProfilePreferencesDTO() { }
	
	private ProfilePreferencesDTO(Builder builder, int profileId) {
		this.profileId = profileId;
		this.canPostOnMyStream = builder.canPostOnMyStream;
		this.premoderateFollowers = builder.premoderateFollowers;
	}
	
	public Integer getProfileId() {
		return profileId;
	}
	public Visibility getCanPostOnMyStream() {
		return canPostOnMyStream;
	}
	public Boolean isPremoderateFollowers() {
		return premoderateFollowers;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfilePreferencesDTO [profileId=");
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
		private Visibility canPostOnMyStream;
		private Boolean premoderateFollowers;

		private Builder() {
		}

		public Builder withCanPostOnMyStream(Visibility canPostOnMyStream) {
			this.canPostOnMyStream = canPostOnMyStream;
			return this;
		}

		public Builder withPremoderateFollowers(Boolean premoderateFollowers) {
			this.premoderateFollowers = premoderateFollowers;
			return this;
		}

		public ProfilePreferencesDTO build(int profileId) {
			return new ProfilePreferencesDTO(this, profileId);
		}
	}
	
	
}
