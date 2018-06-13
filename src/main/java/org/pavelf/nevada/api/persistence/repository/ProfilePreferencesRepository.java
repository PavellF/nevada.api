package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.ProfilePreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePreferencesRepository 
	extends JpaRepository<ProfilePreferences, Integer> {

}
