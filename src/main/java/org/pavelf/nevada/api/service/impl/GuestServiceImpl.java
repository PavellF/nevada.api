package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Guest;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.GuestRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestServiceImpl implements GuestService {

	private GuestRepository guestRepository;
	private ProfileRepository profileRepository;
	
	@Autowired
	public GuestServiceImpl(GuestRepository guestRepository,
			ProfileRepository profileRepository) {
		this.guestRepository = guestRepository;
		this.profileRepository = profileRepository;
	}

	@Override
	@Transactional
	public Integer visitUserProfile(int profileId, boolean hidden) {
		
		Guest guest = new Guest();
		guest.setHidden(hidden);
		guest.setWhen(Instant.now());
		guest.setWho(profileRepository.getOne(profileId));
		
		return guestRepository.save(guest).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuestDTO> getAllGuestsForProfile(int profileId, Version version,
			int start, int count, boolean hiddenOnly, Sorting sorting) {
		if (sorting == null || version == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Sort sort = Sort
				.by(sorting.getDirection(), sorting.getDomainProperty());
		Pageable pageable = PageRequest.of(start, count, sort);
		
		return guestRepository
				.findAllForProfile(profileId, hiddenOnly, pageable)
				.stream().map((Guest g) -> {
					GuestDTO guest = GuestDTO.builder()
							.withHidden(g.isHidden())
							.withId(g.getId())
							.withWhen(g.getWhen())
							.withWhoId(g.getWhoId())
							.build();
					return guest;
				}).collect(Collectors.toList());
	}

}
