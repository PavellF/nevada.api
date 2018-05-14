package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
	private int id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "owner_id")
	private Profile owner; 
	
	@Column(name="visibility_level")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility visibility;
	
	@Column(name="date")
	@NotNull
	@Past
	private Instant postDate;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "album_id")
	private Album album;
	
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

	public Profile getOwner() {
		return owner;
	}

	public void setOwner(Profile owner) {
		this.owner = owner;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
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

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

}
