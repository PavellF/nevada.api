package org.pavelf.nevada.api.service.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.metadata.IIOMetadataNode;

import org.pavelf.nevada.api.domain.Picture;
import org.pavelf.nevada.api.service.ImageProcessor;
import org.springframework.stereotype.Service;

/**
 * Basic implementation for {@code ImageProcessor}.
 * @author Pavel F.
 * @since 1.0
 * */
@Service
public class ImageProcessorImpl implements ImageProcessor {

	@Override
	public Picture process(Picture toProcess) {
		if (toProcess == null) {
			throw new IllegalArgumentException("Null passed.");
		}
		
		byte[] raw = toProcess.getData();
		Image img = new Image(new ByteArrayInputStream(raw));
		img.resize(toProcess.getOutputWidth(), toProcess.getOutputLength());
		
		float quality = toProcess.getOutputQuality();
		
		if (quality < 1.0f) {
			img.setOutputQuality(toProcess.getOutputQuality());
		}
		
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
