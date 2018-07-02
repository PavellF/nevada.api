package org.pavelf.nevada.api;

import java.time.Instant;
import java.util.Optional;

import org.pavelf.nevada.api.persistence.domain.Access;
import org.pavelf.nevada.api.persistence.domain.Application;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.repository.ApplicationRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;

/**
 * Security token builder for testing purposes.
 * @author Pavel F.
 * @since 1.0
 * */
public class TestTokenBuilder {

	private Optional<Integer> holderId = Optional.empty();
	private Optional<Integer> applicationId = Optional.empty();
	private Optional<String> token = Optional.empty();
	private Optional<Access> photoAccess = Optional.empty();
	private Optional<Access> messagesAccess = Optional.empty();
	private Optional<Access> friendsAccess = Optional.empty();
	private Optional<Access> accountAccess = Optional.empty();
	private Optional<Access> applicationAccess = Optional.empty();
	private Optional<Access> notificationsAccess = Optional.empty();
	private Optional<Boolean> superToken = Optional.empty();
	private Optional<Access> streamAccess = Optional.empty();
	private Optional<Access> personInfoAccess = Optional.empty();
	
	/**
	 * @param holderId must be already existing holder id or exception will be
	 * thrown while {@link #build()} method will be issued. If not specified
	 * or {@code null} passed new profile will be created.
	 * */
	public TestTokenBuilder withHolder(Integer holderId) {
		this.holderId = Optional.ofNullable(holderId);
		return this;
	}
	
	/**
	 * @param appId must be already existing application id or exception will 
	 * be thrown while {@link #build()} method will be issued. If not specified
	 * or {@code null} passed new application will be created.
	 * */
	public TestTokenBuilder withApplication(Integer appId) {
		this.applicationId = Optional.ofNullable(appId);
		return this;
	}

	/**
	 * Token with this token value will be created, if not specified or 
	 * {@code null} token will be generated.
	 * */
	public TestTokenBuilder setToken(String token) {
		this.token = Optional.ofNullable(token);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setPhotoAccess(Access photoAccess) {
		this.photoAccess = Optional.ofNullable(photoAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setMessagesAccess(Access messagesAccess) {
		this.messagesAccess = Optional.ofNullable(messagesAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setFriendsAccess(Access friendsAccess) {
		this.friendsAccess = Optional.ofNullable(friendsAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setAccountAccess(Access accountAccess) {
		this.accountAccess = Optional.ofNullable(accountAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setApplicationAccess(Access applicationAccess) {
		this.applicationAccess = Optional.ofNullable(applicationAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setNotificationsAccess(Access notificationsAccess) {
		this.notificationsAccess = Optional.ofNullable(notificationsAccess);
		return this;
	}

	/**
	 * Sets whether this to token has super access. If ignored {@code false} 
	 * will be set.
	 * */
	public TestTokenBuilder setSuperToken(boolean superToken) {
		this.superToken = Optional.ofNullable(superToken);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setStreamAccess(Access streamAccess) {
		this.streamAccess = Optional.ofNullable(streamAccess);
		return this;
	}

	/**
	 * Sets access level for specific resource. If ignored or {@code null} 
	 * passed {@code Access.NONE} will be set.
	 * */
	public TestTokenBuilder setPersonInfoAccess(Access personInfoAccess) {
		this.personInfoAccess = Optional.ofNullable(personInfoAccess);
		return this;
	}
	
	/**
	 * Builds and creates new token based on provided information.
	 * */
	public Token build(ProfileRepository pr, 
			ApplicationRepository applicationRepository,
			TokenRepository tokenRepository) {
		
		final Token newToken = new Token();
		newToken.setAccountAccess(accountAccess.orElse(Access.NONE));
    	newToken.setApplicationAccess(this.applicationAccess.orElse(Access.NONE));
    	newToken.setFriendsAccess(friendsAccess.orElse(Access.NONE));
    	newToken.setMessagesAccess(messagesAccess.orElse(Access.NONE));
    	newToken.setNotificationsAccess(notificationsAccess.orElse(Access.NONE));
    	newToken.setPersonInfoAccess(personInfoAccess.orElse(Access.NONE));
    	newToken.setPhotoAccess(photoAccess.orElse(Access.NONE));
    	newToken.setStreamAccess(streamAccess.orElse(Access.NONE));
    	newToken.setValidUntil(Instant.now().plusSeconds(9999999));
    	newToken.setSuperToken(this.superToken.orElse(false));
    	newToken.setToken(this.token.orElse(generateToken()));
    	
    	final String tokenValue = newToken.getToken();
    	
    	newToken.setBelongsToProfile(this.holderId.orElseGet(() -> {
			Profile newHolder = new Profile();
	    	newHolder.setEmail(tokenValue + "@test.com");
	    	newHolder.setPassword("secret".toCharArray());
	    	newHolder.setSignDate(Instant.now());
	    	newHolder.setUsername("Joe Tester " + tokenValue);
	    	pr.save(newHolder);
	    	return newHolder.getId();
		}));
    	
		newToken.setIssuedBy(this.applicationId.orElseGet(() -> {
			Application application = new Application();
	    	application.setAccessKey(tokenValue + "app");
	    	application.setBelongsTo(newToken.getBelongsToProfile());
	    	application.setSince(Instant.now());
	    	application.setTitle("Testing app " + tokenValue);
	    	applicationRepository.save(application);
			return application.getId();
			
		}));
		
		tokenRepository.save(newToken);
		
		return newToken;
	}

	private String generateToken() {
		return "testToken" + (int) (Math.random() * 10E6);
	}
	
	public static TestTokenBuilder get() {
		return new TestTokenBuilder();
	}
	
}
