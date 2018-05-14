package org.pavelf.nevada.api.domain;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.pavelf.nevada.api.persistence.domain.Album;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Visibility;

public class PhotoDTO {

	private int id;
	private ProfileDTO owner; 
	private Visibility visibility;
	private Instant postDate;
	private AlbumDTO album;
	private byte[] small;
	private byte[] medium;
	private byte[] original;
	
	
	
	public int getId() {
		return id;
	}
	
	public ProfileDTO getOwner() {
		return owner;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
	public Instant getPostDate() {
		return postDate;
	}
	
	public AlbumDTO getAlbum() {
		return album;
	}
	
	public byte[] getSmall() {
		return small;
	}
	
	public byte[] getMedium() {
		return medium;
	}
	
	public byte[] getOriginal() {
		return original;
	}
	
	
	
	
	
	
	
}
