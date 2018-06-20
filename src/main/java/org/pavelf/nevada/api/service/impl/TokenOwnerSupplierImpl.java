package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;
import org.pavelf.nevada.api.domain.Scope;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.security.TokenOwnerSupplier;
import org.pavelf.nevada.api.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements {@code TokenOwnerSupplier} - methods to 
 * obtain authorization token.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class TokenOwnerSupplierImpl implements TokenOwnerSupplier {

	private TokenRepository tokenRepository;
	
	@Autowired
	public TokenOwnerSupplierImpl(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	@Override
	public org.pavelf.nevada.api.security.Token fetchToken(String token) {
		if (token == null) {
			return null;
		}
		return tokenRepository.findByToken(token)
				.map((Token t) -> {
					if (t.getValidUntil().isBefore(Instant.now())) {
						return null;
					} 
					
					Profile profile = t.getProfile();
					User user = new User(profile.getUsername(), 
							String.valueOf(profile.getId()), 
							profile.getEmail());
					
					return new org.pavelf.nevada.api.security.Token(
									t.getToken().toCharArray(), 
									mapTokenScopes(t), 
									t.isSuperToken(), user);
				}).orElse(null);
	}
		
	private Map<String, Integer> mapTokenScopes(Token token) {
		Map<String, Integer> map = new HashMap<>();
		map.put(Scope.PHOTO , token.getPhotoAccess().getLevel());
		map.put(Scope.ACCOUNT , token.getAccountAccess().getLevel());
		map.put(Scope.FRIENDS , token.getFriendsAccess().getLevel());
		map.put(Scope.MESSAGE, token.getMessagesAccess().getLevel());
		map.put(Scope.NOTIFICATION ,token.getNotificationsAccess().getLevel());
		map.put(Scope.STREAM, token.getStreamAccess().getLevel());
		map.put(Scope.ACCOUNT, token.getAccountAccess().getLevel());
		map.put(Scope.PERSON_INFO, token.getPersonInfoAccess().getLevel());
		
		return map;
	}
	
}
