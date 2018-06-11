package org.pavelf.nevada.api.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.HttpUtil;
import org.pavelf.nevada.api.HttpUtil.Algorithm;
import org.pavelf.nevada.api.domain.TokenDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Token;
import org.pavelf.nevada.api.persistence.repository.ApplicationRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TokenRepository;
import org.pavelf.nevada.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

	private TokenRepository tokenRepository;
	private ProfileRepository profileRepository;
	private ApplicationRepository applicationRepository;
	
	private final Function<? super Token, ? extends TokenDTO> tokenMapper = (Token val) -> {
		return TokenDTO.builder()
				.withId(val.getId())
				.withAccountAccess(val.getAccountAccess())
				.withApplicationId(val.getIssuedBy())
				.withFriendsAccess(val.getFriendsAccess())
				.withMessagesAccess(val.getMessagesAccess())
				.withNotificationsAccess(val.getNotificationsAccess())
				.withPhotoAccess(val.getNotificationsAccess())
				.withProfileId(val.getBelongsToProfile())
				.withStreamAccess(val.getStreamAccess())
				.withSuperToken(val.isSuperToken())
				.withPersonInfoAccess(val.getPersonInfoAccess())
				.withToken(val.getToken())
				.withApplicationAccess(val.getApplicationAccess())
				.withValidUntil(val.getValidUntil())
			.build();
	};
	
	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository,
			ProfileRepository profileRepository,
			ApplicationRepository applicationRepository) {
		this.tokenRepository = tokenRepository;
		this.profileRepository = profileRepository;
		this.applicationRepository = applicationRepository;
	}

	@Override
	@Transactional
	public Integer create(TokenDTO token, Version version) {
		if (token == null) {
			throw new IllegalArgumentException();
		}
		Token newToken = new Token();
		newToken.setAccountAccess(token.getAccountAccess());
		newToken.setFriendsAccess(token.getFriendsAccess());
		newToken.setMessagesAccess(token.getMessagesAccess());
		newToken.setNotificationsAccess(token.getNotificationsAccess());
		newToken.setPhotoAccess(token.getPhotoAccess());
		newToken.setProfile(profileRepository.getOne(token.getProfileId()));
		newToken.setStreamAccess(token.getStreamAccess());
		newToken.setSuperToken(token.isSuperToken());
		newToken.setApplicationAccess(token.getApplicationAccess());
		newToken.setToken(HttpUtil.hash(Algorithm.MD5, token));
		newToken.setUssuedBy(applicationRepository.getOne(token.getApplicationId()));
		newToken.setValidUntil(token.getValidUntil());
		
		return tokenRepository.save(newToken).getId();
	}

	/*@Override
	@Transactional(readOnly=true)
	public TokenDTO get(int id, Version version) {
		return tokenRepository.findById(id).map(val -> 
			TokenDTO.builder()
					.withId(val.getId())
					.withAccountAccess(val.getAccountAccess())
					.withApplicationId(val.getIssuedBy())
					.withFriendsAccess(val.getFriendsAccess())
					.withMessagesAccess(val.getMessagesAccess())
					.withNotificationsAccess(val.getNotificationsAccess())
					.withPhotoAccess(val.getNotificationsAccess())
					.withProfileId(val.getBelongsToProfile())
					.withStreamAccess(val.getStreamAccess())
					.withSuperToken(val.isSuperToken())
					.withToken(val.getToken())
					.withValidUntil(val.getValidUntil())
			.build()
		).orElse(null);
	}
*/


	@Override
	public boolean update(TokenDTO token, Version version) {
		if (token == null || version == null || token.getId() == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		Token newToken = new Token();
		newToken.setId(token.getId());
		newToken.setValidUntil(token.getValidUntil());
		
		tokenRepository.save(newToken);
		
		return true;
	}

	@Override
	public List<TokenDTO> getAllForProfile(int profileId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return tokenRepository.findAllByBelongsToProfile(profileId).stream()
				.map(tokenMapper).collect(Collectors.toList());
	}

	@Override
	public List<TokenDTO> getAllForApplication(int applicationId, Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return tokenRepository.findAllByIssuedBy(applicationId).stream()
				.map(tokenMapper).collect(Collectors.toList());
	}

}
