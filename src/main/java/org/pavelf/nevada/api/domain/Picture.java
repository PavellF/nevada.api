package org.pavelf.nevada.api.domain;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Describes processed picture. Mutable.
 * @since 1.0
 * @author Pavel F.
 * */
public class Picture {

	private byte[] data;
	private final String outputFilename;
	private final int outputWidth;
	private final int outputLength;
	private final boolean metadataPreserved;
	private final float outputQuality;
	private BiFunction<String, String, List<String>> metadataSupplier; 
	
	/**
	 * @throws IllegalArgumentException if {@link data} is null.
	 * */
	public Picture(byte[] data, int outputWidth, int outputLength,
			boolean metadataPreserved, float outputQuality, String outputFilename) {
		setData(data);
		
		if (outputFilename == null) {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < data.length && i < 16; i++) {
				sb.append(data[i]);
			}
			
			this.outputFilename = sb.toString();
		} else {
			this.outputFilename = outputFilename ;
		}
		
		this.outputWidth = outputWidth;
		this.outputLength = outputLength;
		this.metadataPreserved = metadataPreserved;
		this.outputQuality = outputQuality;
	}
	
	public Picture(byte[] data, int outputWidth, int outputLength,
			boolean metadataPreserved, float outputQuality) {
		this(data, outputWidth, outputLength, metadataPreserved, outputQuality, null);
	}

	/**
	 * @return never {@code null}.
	 * */
	public byte[] getData() {
		return data;
	}
	
	public String getFilename() {
		return this.outputFilename;
	}
	
	/**
	 * @param filename if not set first n bytes will be used as a name.
	 * @throws IllegalArgumentException if data passed is null.
	 * */ 
	public void setData(byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException();
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
	
	public List<String> getMetadataByTag(String tagname, String attributeName) {
		if (metadataSupplier != null) {
			return metadataSupplier.apply(tagname, attributeName);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Picture [filename=");
		builder.append(outputFilename);
		builder.append(", outputWidth=");
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
