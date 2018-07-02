package org.pavelf.nevada.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Picture;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.service.ImageProcessor;
import org.pavelf.nevada.api.service.PageAndSortExtended;
import org.pavelf.nevada.api.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic implementation of {@code PhotoService}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class PhotoServiceImpl implements PhotoService {

	private ImageProcessor imageProcessor;
	private PhotoRepository photoRepository;
	
	@Autowired
	public PhotoServiceImpl(ImageProcessor imageProcessor,
			PhotoRepository photoRepository) {
		this.imageProcessor = imageProcessor;
		this.photoRepository = photoRepository;
	}

	private final Function<? super Sorting, ? extends Order> propertyMapper = 
			(Sorting s) -> {
				switch (s) {
				case TIME_ASC: return Order.asc("id");
				case TIME_DESC: return Order.desc("id");
				default: return null;
				}
	};
	
	Function<? super Photo, ? extends PhotoDTO> mapper = (Photo p) -> {
		PhotoDTO photo = PhotoDTO.builder()
				.withArchived(p.isArchived())
				.withFileName(p.getFileName())
				.withId(p.getId())
				.withMessage(p.getMessage())
				.withOwnerId(p.getOwnerId())
				.withPostDate(p.getPostDate())
				.withVisibility(p.getVisibility())
				.build();
		return photo;
	};
	
	private Pageable getPageable(PageAndSortExtended params) {
		
		Sort sort = Sort.by(params.getOrderBy().map(propertyMapper)
				.filter(o -> o != null)
				.collect(Collectors.toList()));
		
		return PageRequest.of(params.getStartIndex(),params.getCount(), sort);
	}

	public static void main(String... args) throws IOException {
		Image img = new Image(new File("/Users/macuser/Desktop/test.jpg"));
		img.resize(1500, 1500);
		img.setIIOMetadata(img.getIIOMetadata());
		img.setOutputQuality(0.85);
		Files.write(Paths.get("/Users/macuser/Desktop/jpg.png"), 
				img.getByteArray());
	}

	@Transactional
	@Override
	public void update(PhotoDTO photo, Version version) {
		final Integer id = photo.getId();
		
		if (photo == null || version == null || id == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Photo toUpdate = new Photo();
		toUpdate.setId(id);
		toUpdate.setArchived(photo.isArchived());
		toUpdate.setFileName(photo.getFileName());
		toUpdate.setMessage(photo.getMessage());
		toUpdate.setVisibility(photo.getVisibility());
		
		photoRepository.save(toUpdate);
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] getRawImageById(int id, Size size, Set<Visibility> levels) {
		if (size == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Tuple result = null;
		
		switch (size) {
		case MEDIUM: {
			result = (levels == null) 
					? photoRepository.getMediumRaw(id)
					: photoRepository.getMediumRaw(id, levels);
		} break;
		case ORIGINAL: {
			result = (levels == null) 
					? photoRepository.getOriginalRaw(id)
					: photoRepository.getOriginalRaw(id, levels);
		} break;
		case SMALL: {
			result = (levels == null) 
					? photoRepository.getSmallRaw(id)
					: photoRepository.getSmallRaw(id, levels);
		} break;
		}
		
		if (result != null && result.toArray().length == 1) {
			return result.get(0, byte[].class);
		}
		
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PhotoDTO> getAllForStreamPost(int postId,
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return photoRepository.getAllForPost(postId, getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<PhotoDTO> getAllForMessage(int messageId,
			PageAndSortExtended params) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		return photoRepository.getAllForMessage(messageId, getPageable(params))
				.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<PhotoDTO> getAllForProfile(int profileId,
			PageAndSortExtended params, Set<Visibility> levels) {
		if (params == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Pageable pageable = getPageable(params);
		
		List<Photo> result = (levels == null) 
				? photoRepository.getAllForOwner(profileId, pageable)
				: photoRepository.getAllForOwner(profileId, levels, pageable);
		
		return result.stream().map(mapper).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Integer post(PhotoDTO photo, byte[] raw, Version version) {
		if (photo == null || version == null || raw == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		Instant now = Instant.now();
		String fileName = photo.getFileName();
		
		Photo toSave = new Photo();
		toSave.setArchived(false);
		toSave.setFileName(fileName == null 
				? String.valueOf(now.getLong(ChronoField.INSTANT_SECONDS)) 
				: fileName);
		toSave.setMessage(photo.getMessage());
		toSave.setOwnerId(photo.getOwnerId());
		toSave.setPostDate(now);
		toSave.setVisibility(photo.getVisibility());
		
		Picture small = Picture.newPicture(raw, 76, 76, true, 0.85f);
		imageProcessor.process(small);
		toSave.setSmall(small.getData());
		
		Picture medium = Picture.newPicture(raw, 340, 340, true, 1f);
		imageProcessor.process(medium);
		toSave.setMedium(medium.getData());
		
		toSave.setOriginal(raw);
		
		return photoRepository.save(toSave).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean areBelongsTo(int profileId, Collection<Integer> photoIds) {
		if (photoIds == null) {
			throw new IllegalArgumentException("Null is not allowed.");
		}
		
		int count = photoRepository
				.countPhotosBelongProfile(photoIds, profileId);
		
		return count == photoIds.size();
	}

	@Override
	public boolean isBelongsTo(int profileId, int photoIds) {
		return areBelongsTo(profileId, Arrays.asList(photoIds));
	}
}
