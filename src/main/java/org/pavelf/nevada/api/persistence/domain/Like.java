package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 * ORM {@code @Entity} that represent user like.
 * @author Pavel F.
 * @since 1.0
 * */
@Entity
@Table(name="_likes")
public class Like {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "liked_message")
	private Integer likedMessage;
	
	@Column(name = "liked_stream_post")
	private Integer likedStreamPost;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "by_user", insertable = false, updatable = false)
	private Profile likedBy;
	
	@Column(name = "by_user")
	@NotNull
	private Integer likedById;
	
	@Column(name="date")
	@NotNull
	@Past
	private Instant date;
	
	@Column(name="rating")
	private short rating;	
	
	public Like() {
		
	}

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

	public Integer getLikedMessage() {
		return likedMessage;
	}

	public void setLikedMessage(Integer likedMessage) {
		this.likedMessage = likedMessage;
	}

	public Integer getLikedStreamPost() {
		return likedStreamPost;
	}

	public void setLikedStreamPost(Integer likedStreamPost) {
		this.likedStreamPost = likedStreamPost;
	}

	public Profile getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Profile likedBy) {
		this.likedBy = likedBy;
	}

	public Integer getLikedById() {
		return likedById;
	}

	public void setLikedById(Integer likedById) {
		this.likedById = likedById;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public short getRating() {
		return rating;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}


}
