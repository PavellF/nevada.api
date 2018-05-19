package org.pavelf.nevada.api.service;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.domain.ProfileDTO;
import org.pavelf.nevada.api.domain.Version;

/**
 * General interface for interactions with user profile.
 * @since 1.0
 * @author Pavel F.
 * */
public interface ProfileService {
	
	/**
	 * Creates new user in the application.
	 * @param profile profile which is base for new one.
	 * @return never {@code null}, mutated ProfileDTO.
	 */
	public ProfileDTO create(ProfileDTO profile);
	
	
}
