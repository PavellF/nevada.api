package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
@Table(name="photos")
public class Photo {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "message")
	private String message;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "owner_id", insertable = false, updatable = false)
	private Profile owner; 
	
	@Column(name = "owner_id")
	private int ownerId;
	
	@Column(name="date")
	@NotNull
	@Past
	private Instant postDate;
	
	@Column(name="small")
	@NotNull
	@Basic(fetch=FetchType.LAZY)
	private byte[] small;
	
	@Column(name="medium")
	@NotNull
	@Basic(fetch=FetchType.LAZY)
	private byte[] medium;
	
	@Column(name="original")
	@NotNull
	@Basic(fetch=FetchType.LAZY)
	private byte[] original;
	
	@Column(name="message_photo")
	private Integer associatedMessageId;
	
	@Column(name="stream_post_photo")
	private Integer associatedStreamPostId;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Profile getOwner() {
		return owner;
	}

	public void setOwner(Profile owner) {
		this.owner = owner;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public Instant getPostDate() {
		return postDate;
	}

	public void setPostDate(Instant postDate) {
		this.postDate = postDate;
	}

	public byte[] getSmall() {
		return small;
	}

	public void setSmall(byte[] small) {
		this.small = small;
	}

	public byte[] getMedium() {
		return medium;
	}

	public void setMedium(byte[] medium) {
		this.medium = medium;
	}

	public byte[] getOriginal() {
		return original;
	}

	public void setOriginal(byte[] original) {
		this.original = original;
	}

	public Integer getAssociatedMessageId() {
		return associatedMessageId;
	}

	public void setAssociatedMessageId(Integer associatedMessageId) {
		this.associatedMessageId = associatedMessageId;
	}

	public Integer getAssociatedStreamPostId() {
		return associatedStreamPostId;
	}

	public void setAssociatedStreamPostId(Integer associatedStreamPostId) {
		this.associatedStreamPostId = associatedStreamPostId;
	}

	

}
