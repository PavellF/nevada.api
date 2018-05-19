package org.pavelf.nevada.api.domain;

/**
 * Tag that can relate to something.
 * @since 1.0
 * @author Pavel F.
 * */
public class TagDTO {

	private String tagName;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TagDTO [tagName=");
		builder.append(tagName);
		builder.append("]");
		return builder.toString();
	}

}
