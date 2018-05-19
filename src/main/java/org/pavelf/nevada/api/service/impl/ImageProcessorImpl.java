package org.pavelf.nevada.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.metadata.IIOMetadataNode;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.pavelf.nevada.api.domain.Picture;
import org.pavelf.nevada.api.service.ImageProcessor;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessorImpl implements ImageProcessor {

	@Override
	public Picture process(Picture toProcess) {
		if (toProcess == null) {
			throw new IllegalArgumentException();
		}
		
		byte[] raw = toProcess.getData();
		Image img = new Image(new ByteArrayInputStream(raw));
		img.resize(toProcess.getOutputWidth(), toProcess.getOutputLength());
		img.setOutputQuality(toProcess.getOutputQuality());
		
		if (toProcess.isMetadataPreserved()) {
			img.setIIOMetadata(img.getIIOMetadata());
			
			toProcess.setMetadataSupplier((String tagname, String attr) -> {
				List<String> list  = new ArrayList<>();
				IIOMetadataNode[] nodes = img.getMetadataByTagName(tagname);
				for (IIOMetadataNode node : nodes) {
					list.add(node.getAttribute(attr));
				}
				return list;
			});
		}
		
		toProcess.setData(img.getByteArray());
		return toProcess;
	}

	


}
