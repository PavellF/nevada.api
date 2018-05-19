package org.pavelf.nevada.api.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Application;
import org.pavelf.nevada.api.persistence.domain.Profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents application security token.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class TokenDTO {

	private Integer id;
	private String token;
	private Instant validUntil;
	private Access photoAccess;
	private Access messagesAccess;
	private Access friendsAccess;
	private Access accountAccess;
	private Access notificationsAccess;
	private boolean superToken;
	private Access chatAccess;
	private Access streamAccess;
	private Profile profile; 
	private Integer profileId;
	private Application ussuedBy; 
	private Integer applicationId;
	private transient Version version;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Instant getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Instant validUntil) {
		this.validUntil = validUntil;
	}
	public Access getPhotoAccess() {
		return photoAccess;
	}
	public void setPhotoAccess(Access photoAccess) {
		this.photoAccess = photoAccess;
	}
	public Access getMessagesAccess() {
		return messagesAccess;
	}
	public void setMessagesAccess(Access messagesAccess) {
		this.messagesAccess = messagesAccess;
	}
	public Access getFriendsAccess() {
		return friendsAccess;
	}
	public void setFriendsAccess(Access friendsAccess) {
		this.friendsAccess = friendsAccess;
	}
	public Access getAccountAccess() {
		return accountAccess;
	}
	public void setAccountAccess(Access accountAccess) {
		this.accountAccess = accountAccess;
	}
	public Access getNotificationsAccess() {
		return notificationsAccess;
	}
	public void setNotificationsAccess(Access notificationsAccess) {
		this.notificationsAccess = notificationsAccess;
	}
	public boolean isSuperToken() {
		return superToken;
	}
	public void setSuperToken(boolean superToken) {
		this.superToken = superToken;
	}
	public Access getChatAccess() {
		return chatAccess;
	}
	public void setChatAccess(Access chatAccess) {
		this.chatAccess = chatAccess;
	}
	public Access getStreamAccess() {
		return streamAccess;
	}
	public void setStreamAccess(Access streamAccess) {
		this.streamAccess = streamAccess;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public Application getUssuedBy() {
		return ussuedBy;
	}
	public void setUssuedBy(Application ussuedBy) {
		this.ussuedBy = ussuedBy;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TokenDTO [id=");
		builder.append(id);
		builder.append(", token=");
		builder.append(token);
		builder.append(", validUntil=");
		builder.append(validUntil);
		builder.append(", photoAccess=");
		builder.append(photoAccess);
		builder.append(", messagesAccess=");
		builder.append(messagesAccess);
		builder.append(", friendsAccess=");
		builder.append(friendsAccess);
		builder.append(", accountAccess=");
		builder.append(accountAccess);
		builder.append(", notificationsAccess=");
		builder.append(notificationsAccess);
		builder.append(", superToken=");
		builder.append(superToken);
		builder.append(", chatAccess=");
		builder.append(chatAccess);
		builder.append(", streamAccess=");
		builder.append(streamAccess);
		builder.append(", profileId=");
		builder.append(profileId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", version=");
		builder.append(version);
		builder.append("]");
		return builder.toString();
	}
	
	
}
