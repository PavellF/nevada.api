package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.ProfilePreferences;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Defines set of actions with external storage of {@code ProfilePreferences}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface ProfilePreferencesRepository 
	extends JpaRepository<ProfilePreferences, Integer> {

}
