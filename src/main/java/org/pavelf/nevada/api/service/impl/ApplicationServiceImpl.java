package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

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
			throw new IllegalArgumentException("Nulls are not allowed.");
		}
		
		Application newApplication = new Application();
		newApplication.setProfile(profileRepository.getOne(application.getProfileId()));
		newApplication.setAccessKey(HttpUtil.hash(Algorithm.MD5, application.getTitle()));
		newApplication.setSince(Instant.now());
		newApplication.setSuspendedUntil(null);
		newApplication.setTitle(application.getTitle());
		
		return applicationRepository.save(newApplication).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ApplicationDTO> getAllForProfile(int profileId,
			Version version) {
		if (version == null) {
			throw new IllegalArgumentException("Nulls are not allowed.");
		}
		return applicationRepository.getAllByBelongsTo(profileId).stream().map((Application a) -> {
			return ApplicationDTO.builder().withAccessKey(a.getAccessKey())
			.withId(a.getId())
			.withProfileId(a.getBelongsTo())
			.withSince(a.getSince())
			.withSuspendedUntil(a.getSuspendedUntil())
			.withTitle(a.getTitle()).build();
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean update(ApplicationDTO application, Version version) {
		if (application == null || version == null) {
			throw new IllegalArgumentException("Nulls are not allowed.");
		}
		
		Application newApplication = new Application();
		newApplication.setId(application.getId());
		
		if (application.getProfileId() != null) {
			newApplication.setProfile(profileRepository.getOne(application.getProfileId()));
		}
		
		final String newTitle = application.getTitle();
		if (newTitle != null) {
			newApplication.setTitle(newTitle);
			newApplication.setAccessKey(HttpUtil.hash(Algorithm.MD5, newTitle));
		}
		
		newApplication.setSuspendedUntil(application.getSuspendedUntil());
		applicationRepository.save(newApplication);
		
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isBelongsTo(int profileId, int applicationId) {
		return applicationRepository.countByIdAndBelongsTo(applicationId, profileId) == 1;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isSuspended(int applicationId) {
		return applicationRepository.isSuspended(applicationId, Instant.now()) == 1;
	}

}
