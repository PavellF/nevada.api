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
	private boolean premoderateFollowers;
	
	public Integer getProfileId() {
		return profileId;
	}
	public Visibility getCanPostOnMyStream() {
		return canPostOnMyStream;
	}
	public boolean isPremoderateFollowers() {
		return premoderateFollowers;
	}
	
}
