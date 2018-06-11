package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="profile_preferences")
public class ProfilePreferences {

	@Id
    @Column(name = "profile_id")
	private int profileId;
	
	@JoinColumn(name = "profile_id", insertable = false, updatable = false)
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	private Profile profile;
	
	@Column(name="can_post_on_my_stream")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility canPostOnMyStream;
	
	/**
	 * Does NOT trigger lazy loading. Always returns empty string.
	 * */
	@Override
	public String toString() {
		return "";
	}

	/**
	 * Does NOT trigger lazy loading. Works similar to default implementation.
	 * */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Does NOT trigger lazy loading. Always throws.
	 * @throws UnsupportedOperationException
	 * */
	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Visibility getCanPostOnMyStream() {
		return canPostOnMyStream;
	}

	public void setCanPostOnMyStream(Visibility canPostOnMyStream) {
		this.canPostOnMyStream = canPostOnMyStream;
	}
	
}
