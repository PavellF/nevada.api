package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
@Table(name="_likes")
public class Like {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinTable(name = "liked_messages", joinColumns = { @JoinColumn(name = "like_id") }, 
    inverseJoinColumns = { @JoinColumn(name = "liked_message_id")} )
	private Message message;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "by_user", insertable = false, updatable = false)
	private Profile likedBy;
	
	@Column(name = "by_user")
	private int likedById;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message msg) {
		this.message = msg;
	}

	public Profile getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Profile likedBy) {
		this.likedBy = likedBy;
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

	public int getLikedById() {
		return likedById;
	}

	public void setLikedById(int likedById) {
		this.likedById = likedById;
	}
}
