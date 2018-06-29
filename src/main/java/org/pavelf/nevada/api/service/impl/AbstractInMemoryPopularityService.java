package org.pavelf.nevada.api.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.pavelf.nevada.api.service.PopularityService;

/**
 * Implements some of methods of {@code PopularityService} in order to help 
 * interface implementors.
 * @author Pavel F.
 * @since 1.0
 * */
public abstract class AbstractInMemoryPopularityService 
	implements PopularityService {

	protected final ConcurrentHashMap<Integer, AtomicInteger> idToRatingMap = 
			new ConcurrentHashMap<>();
	
	@Override
	public void increaseBy(int identifier, int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Negative popularity value "
					+ "has no sense.");
		}
		
		AtomicInteger currentRating = idToRatingMap.get(identifier);
		
		if (currentRating == null) {
			idToRatingMap.put(identifier, new AtomicInteger(value));
		} else {
			currentRating.addAndGet(value);
		}

	}

	@Override
	public void increase(int identifier) {
		
		AtomicInteger currentRating = idToRatingMap.get(identifier);
		
		if (currentRating == null) {
			idToRatingMap.put(identifier, new AtomicInteger(1));
		} else {
			currentRating.incrementAndGet();
		}

	}

	@Override
	public boolean clear(int identifier) {
		return idToRatingMap.remove(identifier) == null;
	}

	@Override
	public abstract void clearAll();

}
