package org.pavelf.nevada.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Picture;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.ImageProcessor;
import org.pavelf.nevada.api.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PhotoServiceImpl implements PhotoService {

	private ImageProcessor imageProcessor;
	private ProfileRepository profileRepository;
	private PhotoRepository photoRepository;
	private TagRepository tagRepository;
	
	@Transactional
	@Override
	public Integer post(PhotoDTO photo, Version version) {
		if (photo == null || version == null) {
			throw new IllegalArgumentException();
		}
		
		Photo newPhoto = new Photo();
		newPhoto.setOriginal(photo.getOriginal());
		newPhoto.setOwner(profileRepository.getOne(photo.getOwnerId()));//check owner
		newPhoto.setPostDate(Instant.now());
		
		Picture small = new Picture(photo.getOriginal(), 76, 76, true, 0.85f);
		newPhoto.setSmall(imageProcessor.process(small).getData());
		
		Picture medium = new Picture(photo.getOriginal(), 340, 340, true, 1f);
		newPhoto.setMedium(imageProcessor.process(medium).getData());
		
		return photoRepository.save(newPhoto).getId();
	}
	
	public static void main(String... args) throws IOException {
		Image img = new Image(new File("/Users/macuser/Desktop/test.jpg"));
		img.resize(150, 150);
		img.setIIOMetadata(img.getIIOMetadata());
		img.setOutputQuality(0.85);
		Files.write(Paths.get("/Users/macuser/Desktop/h.jpeg"), img.getByteArray());
		
    }

	@Override
	public boolean isBelongsTo(int profileId, int photoId) {
		return this.photoRepository.countByIdAndOwnerId(photoId, profileId) == 1;
	}

	

}
