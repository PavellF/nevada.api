package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.pavelf.nevada.api.persistence.domain.Album;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import javax.annotation.Generated;
import java.util.Collections;
import java.util.Iterator;

public class PhotoDTO {

	private Integer id;
	private ProfileDTO owner; 
	private Integer ownerId;
	private Visibility visibility;
	private Instant postDate;
	private AlbumDTO album;
	private Integer albumId;
	private String message;
	private byte[] small;
	private byte[] medium;
	private byte[] original;
	private Set<LikeDTO> likes;
	private Set<TagDTO> tags;
	
	public PhotoDTO() { }
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ProfileDTO getOwner() {
		return owner;
	}
	public void setOwner(ProfileDTO owner) {
		this.owner = owner;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
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
	public AlbumDTO getAlbum() {
		return album;
	}
	public void setAlbum(AlbumDTO album) {
		this.album = album;
	}
	public Integer getAlbumId() {
		return albumId;
	}
	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public Set<LikeDTO> getLikes() {
		return likes;
	}
	public void setLikes(Set<LikeDTO> likes) {
		this.likes = likes;
	}
	public Set<TagDTO> getTags() {
		return tags;
	}
	public void setTags(Set<TagDTO> tags) {
		this.tags = tags;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PhotoDTO [id=");
		builder.append(id);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append(", visibility=");
		builder.append(visibility);
		builder.append(", postDate=");
		builder.append(postDate);
		builder.append(", albumId=");
		builder.append(albumId);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}
	

}
