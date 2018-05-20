package org.pavelf.nevada.api.service.impl;

import java.time.Instant;

import org.pavelf.nevada.api.HttpUtil;
import org.pavelf.nevada.api.HttpUtil.Algorithm;
import org.pavelf.nevada.api.domain.ApplicationDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Application;
import org.pavelf.nevada.api.persistence.repository.ApplicationRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	private ApplicationRepository applicationRepository;
	private ProfileRepository profileRepository;
	
	@Autowired
	public ApplicationServiceImpl(ApplicationRepository applicationRepository,
			ProfileRepository profileRepository) {
		this.applicationRepository = applicationRepository;
		this.profileRepository = profileRepository;
	}

	@Override
	@Transactional
	public Integer create(ApplicationDTO application, Version version) {
		if (application == null || version == null) {
			throw new IllegalArgumentException();
		}
		
		Application newApplication = new Application();
		newApplication.setProfile(profileRepository.getOne(application.getProfileId()));
		newApplication.setAccessKey(HttpUtil.hash(Algorithm.MD5, application));
		newApplication.setSince(Instant.now());
		newApplication.setSuspendedUntil(null);
		newApplication.setTitle(application.getTitle());
		
		return applicationRepository.save(newApplication).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public ApplicationDTO getExisting(int id, Version version) {
		
		return null;
	}

}
