package org.pavelf.nevada.api.service.impl;

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;


import org.pavelf.nevada.api.domain.PhotoDTO;
import org.pavelf.nevada.api.domain.Picture;
import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.repository.PhotoRepository;
import org.pavelf.nevada.api.persistence.repository.ProfileRepository;
import org.pavelf.nevada.api.persistence.repository.TagRepository;
import org.pavelf.nevada.api.service.ImageProcessor;
import org.pavelf.nevada.api.service.PhotoService;
import org.pavelf.nevada.api.service.TagsService;
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

	

}
