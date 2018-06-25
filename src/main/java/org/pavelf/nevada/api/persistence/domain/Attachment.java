package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * {@code @Entity} that links <b>many</b> {@code StreamPost}s or 
 * {@code Message}s <b>to many</b> {@code Tag}s or {@code Photo}s.
 * @author Pavel F.
 * @since 1.0
 * */
@Entity
@Table(name="attachment")
public class Attachment {

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "to_stream_post")
	private Integer toStreamPost;
	
	@Column(name = "to_message")
	private Integer toMessage;
	
	@Column(name = "photo")
	private Integer photoId;
	
	@Column(name = "tag")
	private String tagName;
	
	/**
	 * Does NOT trigger lazy loading. Always returns empty string.
	 * */
	@Override
	public String toString() {
		return "";
	}

	/**
	 * Does NOT trigger lazy loading. Works similar to default implementation.
	 * */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Does NOT trigger lazy loading. Always throws.
	 * @throws UnsupportedOperationException
	 * */
	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getToStreamPost() {
		return toStreamPost;
	}
 
	public void setToStreamPost(Integer toStreamPost) {
		this.toStreamPost = toStreamPost;
	}

	public Integer getToMessage() {
		return toMessage;
	}

	public void setToMessage(Integer toMessage) {
		this.toMessage = toMessage;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
