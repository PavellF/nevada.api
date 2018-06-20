package org.pavelf.nevada.api.domain;

import java.io.Serializable;
import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Access;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents application security token.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 4205881149429367060L;
	private Integer id;
	private String token;
	private Instant validUntil;
	private Access photoAccess;
	private Access messagesAccess;
	private Access friendsAccess;
	private Access accountAccess;
	private Access notificationsAccess;
	private Access applicationAccess;
	private Boolean superToken;
	private Access chatAccess;
	private Access streamAccess;
	private Access personInfoAccess;
	private Integer profileId;
	private Integer applicationId;

	private TokenDTO(Builder builder) {
		this.id = builder.id;
		this.token = builder.token;
		this.validUntil = builder.validUntil;
		this.photoAccess = builder.photoAccess;
		this.messagesAccess = builder.messagesAccess;
		this.friendsAccess = builder.friendsAccess;
		this.accountAccess = builder.accountAccess;
		this.notificationsAccess = builder.notificationsAccess;
		this.applicationAccess = builder.applicationAccess;
		this.superToken = builder.superToken;
		this.chatAccess = builder.chatAccess;
		this.streamAccess = builder.streamAccess;
		this.personInfoAccess = builder.personInfoAccess;
		this.profileId = builder.profileId;
		this.applicationId = builder.applicationId;
	}

	private TokenDTO() { }

	public Integer getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public Instant getValidUntil() {
		return validUntil;
	}

	public Access getPhotoAccess() {
		return photoAccess;
	}

	public Access getMessagesAccess() {
		return messagesAccess;
	}

	public Access getFriendsAccess() {
		return friendsAccess;
	}

	public Access getAccountAccess() {
		return accountAccess;
	}

	public Access getNotificationsAccess() {
		return notificationsAccess;
	}

	public Access getApplicationAccess() {
		return applicationAccess;
	}

	public Boolean isSuperToken() {
		return superToken;
	}

	public Access getChatAccess() {
		return chatAccess;
	}

	public Access getStreamAccess() {
		return streamAccess;
	}

	public Access getPersonInfoAccess() {
		return personInfoAccess;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public Integer getApplicationId() {
		return applicationId;
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
		builder.append(", applicationAccess=");
		builder.append(applicationAccess);
		builder.append(", superToken=");
		builder.append(superToken);
		builder.append(", chatAccess=");
		builder.append(chatAccess);
		builder.append(", streamAccess=");
		builder.append(streamAccess);
		builder.append(", personInfoAccess=");
		builder.append(personInfoAccess);
		builder.append(", profileId=");
		builder.append(profileId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link TokenDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link TokenDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private String token;
		private Instant validUntil;
		private Access photoAccess;
		private Access messagesAccess;
		private Access friendsAccess;
		private Access accountAccess;
		private Access notificationsAccess;
		private Access applicationAccess;
		private Boolean superToken;
		private Access chatAccess;
		private Access streamAccess;
		private Access personInfoAccess;
		private Integer profileId;
		private Integer applicationId;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withToken(String token) {
			this.token = token;
			return this;
		}

		public Builder withValidUntil(Instant validUntil) {
			this.validUntil = validUntil;
			return this;
		}

		public Builder withPhotoAccess(Access photoAccess) {
			this.photoAccess = photoAccess;
			return this;
		}

		public Builder withMessagesAccess(Access messagesAccess) {
			this.messagesAccess = messagesAccess;
			return this;
		}

		public Builder withFriendsAccess(Access friendsAccess) {
			this.friendsAccess = friendsAccess;
			return this;
		}

		public Builder withAccountAccess(Access accountAccess) {
			this.accountAccess = accountAccess;
			return this;
		}

		public Builder withNotificationsAccess(Access notificationsAccess) {
			this.notificationsAccess = notificationsAccess;
			return this;
		}

		public Builder withApplicationAccess(Access applicationAccess) {
			this.applicationAccess = applicationAccess;
			return this;
		}

		public Builder withSuperToken(Boolean superToken) {
			this.superToken = superToken;
			return this;
		}

		public Builder withChatAccess(Access chatAccess) {
			this.chatAccess = chatAccess;
			return this;
		}

		public Builder withStreamAccess(Access streamAccess) {
			this.streamAccess = streamAccess;
			return this;
		}

		public Builder withPersonInfoAccess(Access personInfoAccess) {
			this.personInfoAccess = personInfoAccess;
			return this;
		}

		public Builder withProfileId(Integer profileId) {
			this.profileId = profileId;
			return this;
		}

		public Builder withApplicationId(Integer applicationId) {
			this.applicationId = applicationId;
			return this;
		}

		public TokenDTO build() {
			return new TokenDTO(this);
		}
	}

	
	
}
