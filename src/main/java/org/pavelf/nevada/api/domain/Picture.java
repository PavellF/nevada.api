package org.pavelf.nevada.api.domain;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Describes processed picture. Mutable.
 * @since 1.0
 * @author Pavel F.
 * */
public class Picture {

	private byte[] data;
	private final int outputWidth;
	private final int outputLength;
	private final boolean metadataPreserved;
	private final float outputQuality;
	private BiFunction<String, String, List<String>> metadataSupplier; 
	
	/**
	 * @throws IllegalArgumentException if {@link data} is null.
	 * */
	private Picture(byte[] data, int outputWidth, int outputLength,
			boolean metadataPreserved, float outputQuality) {
		setData(data);
		this.outputWidth = outputWidth;
		this.outputLength = outputLength;
		this.metadataPreserved = metadataPreserved;
		this.outputQuality = outputQuality;
	}
	
	public static Picture newPicture(byte[] data, int outputWidth, 
			int outputLength, boolean metadataPreserved, float outputQuality) {
		return new Picture(data, outputWidth, outputLength, 
				metadataPreserved, outputQuality);
	}
	
	/**
	 * @return never {@code null}.
	 * */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * @throws IllegalArgumentException if data passed is null.
	 * */ 
	public void setData(byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException("Null passed.");
		}
		this.data = data;
	}

	public int getOutputWidth() {
		return outputWidth;
	}

	public int getOutputLength() {
		return outputLength;
	}

	public boolean isMetadataPreserved() {
		return metadataPreserved;
	}

	public float getOutputQuality() {
		return outputQuality;
	}

	public void setMetadataSupplier(
			BiFunction<String, String, List<String>> metadataSupplier) {
		this.metadataSupplier = metadataSupplier;
	}
	
	public List<String> getMetadataByTag(String tagname, 
			String attributeName) {
		if (metadataSupplier != null) {
			return metadataSupplier.apply(tagname, attributeName);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Picture outputWidth=");
		builder.append(outputWidth);
		builder.append(", outputLength=");
		builder.append(outputLength);
		builder.append(", metadataPreserved=");
		builder.append(metadataPreserved);
		builder.append(", outputQuality=");
		builder.append(outputQuality);
		builder.append("]");
		return builder.toString();
	}

	
}
