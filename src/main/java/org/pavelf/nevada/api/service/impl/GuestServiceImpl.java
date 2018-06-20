package org.pavelf.nevada.api.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.GuestDTO;
import org.pavelf.nevada.api.persistence.domain.Guest;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.repository.GuestRepository;
import org.pavelf.nevada.api.service.GuestService;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation for {@code GuestService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class GuestServiceImpl implements GuestService {

	private GuestRepository guestRepository;
	private final Function<? super Sorting, ? extends Order> propertyMapper = 
			(Sorting s) -> {
				if (Sorting.TIME_ASC == s) {
					return Order.asc("id");
				} else if (Sorting.TIME_DESC == s) {
					return Order.desc("id");
				} else {
					return null;
				}
			};
	
	@Autowired
	public GuestServiceImpl(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	@Override
	@Transactional
	public Integer visitUserProfile(int profileId, int who, boolean hidden) {
		
		Guest guest = new Guest();
		guest.setHidden(hidden);
		guest.setWhen(Instant.now());
		guest.setWhoId(who);
		guest.setToProfile(profileId);
		
		
		return guestRepository.save(guest).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuestDTO> getAllGuestsForProfile(
			int profileId, boolean includeHidden, PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		Pageable pageable = PageRequest.of(params.getStartIndex(),
					params.getCount(), sort);
		
		final List<Guest> result;
		
		if (includeHidden) {
			result = guestRepository.findAllForProfile(profileId, pageable);
		} else {
			result = guestRepository
					.findAllForProfile(profileId, includeHidden, pageable);
		}
		
		return result.stream().map((Guest g) -> {
					GuestDTO guest = GuestDTO.builder()
							.withHidden(g.isHidden())
							.withId(g.getId())
							.withWhen(g.getWhen())
							.withWhoId(g.getWhoId())
							.withToProfile(g.getToProfile())
							.build();
					return guest;
				}).collect(Collectors.toList());
	}

}
