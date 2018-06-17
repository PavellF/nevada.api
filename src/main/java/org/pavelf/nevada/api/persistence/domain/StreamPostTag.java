package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * {@code @Entity} that links <b>many</b> {@code StreamPost} 
 * <b>to many</b> {@code Tag}s.
 * @author Pavel F.
 * @since 1.0
 * */
@Entity
@Table(name="stream_post_has_tag")
public class StreamPostTag {

	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "stream_post_id")
	@NotNull
	private Integer associatedStreamPost;
	
	@Column(name = "tag")
	@NotNull
	private String associatedTag;
	
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

	public Integer getAssociatedStreamPost() {
		return associatedStreamPost;
	}

	public void setAssociatedStreamPost(Integer associatedStreamPost) {
		this.associatedStreamPost = associatedStreamPost;
	}

	public String getAssociatedTag() {
		return associatedTag;
	}

	public void setAssociatedTag(String associatedTag) {
		this.associatedTag = associatedTag;
	}

	
}
