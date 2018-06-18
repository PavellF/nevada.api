package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="profile_preferences")
public class ProfilePreferences {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "profile_id")
	private Integer profileId;
	
	@JoinColumn(name = "profile_id", insertable = false, updatable = false)
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	private Profile profile;
	
	@Column(name="can_post_on_my_stream")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility canPostOnMyStream;
	
	@Column(name="premoderate_followers")
	private boolean premoderateFollowers;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
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

	public boolean isPremoderateFollowers() {
		return premoderateFollowers;
	}

	public void setPremoderateFollowers(boolean premoderateFollowers) {
		this.premoderateFollowers = premoderateFollowers;
	}

}
