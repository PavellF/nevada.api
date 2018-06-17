package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="tokens")
@NamedEntityGraph(name = "graph.tokens.profile", 
attributeNodes = @javax.persistence.NamedAttributeNode("profile"))
public class Token {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="token")
	@Size(min = 1, max = 1024)
	private String token;
	
	@Column(name="valid_until")
	@NotNull
	private Instant validUntil;
	
	@Column(name="photo_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access photoAccess;
	
	@Column(name="messages_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access messagesAccess;
	
	@Column(name="friends_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access friendsAccess;
	
	@Column(name="account_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access accountAccess;
	
	@Column(name="application_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access applicationAccess;
	
	@Column(name="notifications_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access notificationsAccess;
	
	@Column(name="is_super_token")
	private boolean superToken;
	
	@Column(name="stream_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access streamAccess;
	
	@Column(name="person_info_access")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Access personInfoAccess;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "belongs_to_profile", insertable = false, updatable = false)
	private Profile profile; 
	
	@Column(name = "belongs_to_profile")
	private int belongsToProfile;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "issued_by", insertable = false, updatable = false)
	private Application issuedByApp; 
	
	@Column(name = "issued_by")
	private int issuedBy;

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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Application getUssuedBy() {
		return issuedByApp;
	}

	public void setUssuedBy(Application ussuedBy) {
		this.issuedByApp = ussuedBy;
	}

	public Access getStreamAccess() {
		return streamAccess;
	}

	public void setStreamAccess(Access streamAccess) {
		this.streamAccess = streamAccess;
	}

	public boolean isSuperToken() {
		return superToken;
	}

	public void setSuperToken(boolean superToken) {
		this.superToken = superToken;
	}

	public int getBelongsToProfile() {
		return belongsToProfile;
	}

	public void setBelongsToProfile(int belongsToProfile) {
		this.belongsToProfile = belongsToProfile;
	}

	public Application getIssuedByApp() {
		return issuedByApp;
	}

	public void setIssuedByApp(Application issuedByApp) {
		this.issuedByApp = issuedByApp;
	}

	public int getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(int issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Access getApplicationAccess() {
		return applicationAccess;
	}

	public void setApplicationAccess(Access applicationAccess) {
		this.applicationAccess = applicationAccess;
	}

	public Access getPersonInfoAccess() {
		return personInfoAccess;
	}

	public void setPersonInfoAccess(Access personInfoAccess) {
		this.personInfoAccess = personInfoAccess;
	}

	
	
	
	
}
