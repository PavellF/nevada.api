package org.pavelf.nevada.api.domain;

import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private ProfileDTO profile; 
	private Integer profileId;
	private Instant suspendedUntil;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Instant getSince() {
		return since;
	}
	public void setSince(Instant since) {
		this.since = since;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public ProfileDTO getProfile() {
		return profile;
	}
	public void setProfile(ProfileDTO profile) {
		this.profile = profile;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public Instant getSuspendedUntil() {
		return suspendedUntil;
	}
	public void setSuspendedUntil(Instant suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
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
	
	
}
