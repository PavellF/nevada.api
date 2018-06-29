package org.pavelf.nevada.api.service.impl;

import org.springframework.stereotype.Service;

/**
 * Basic implementation of {@code PopularityService} for {@code Profile}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class ProfilePopularityService
		extends AbstractInMemoryPopularityService {

	@Override
	//@Scheduled(fixedDelay = 6000000)
	public void clearAll() {
		//has not implemented yet..

	}

}
