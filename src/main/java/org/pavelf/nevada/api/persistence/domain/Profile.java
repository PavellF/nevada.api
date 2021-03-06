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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="profiles")
public class Profile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="username")
	@NotNull
	@Size(min = 1, max = 128)
	private String username;
	
	@Column(name="password")
	@NotNull
	@Size(min = 6, max = 1024)
	private char[] password;
	
	@Column(name="sign_date")
	@NotNull
	private Instant signDate;
	
	@Column(name="email")
	@NotNull
	@javax.validation.constraints.Email
	private String email;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinColumn(name = "about", insertable = false, updatable = false)
	private Message about;
	
	@Column(name = "about")
	private Integer aboutId;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinColumn(name="user_pic", insertable = false, updatable = false)
	private Photo picture;
	
	@Column(name = "user_pic")
	private Integer pictureId;
	
	@Min(0)
	@Column(name="popularity")
	private Integer popularity;
	
	@Column(name="rating")
	private Integer rating;
	
	@Column(name="suspended_until")
	private Instant suspendedUntil;
	
	public Profile() { }

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public Instant getSignDate() {
		return signDate;
	}

	public void setSignDate(Instant signDate) {
		this.signDate = signDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Message getAbout() {
		return about;
	}

	public void setAbout(Message about) {
		this.about = about;
	}

	public Integer getAboutId() {
		return aboutId;
	}

	public void setAboutId(Integer aboutId) {
		this.aboutId = aboutId;
	}

	public Photo getPicture() {
		return picture;
	}

	public void setPicture(Photo picture) {
		this.picture = picture;
	}

	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Instant getSuspendedUntil() {
		return suspendedUntil;
	}

	public void setSuspendedUntil(Instant suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}


	
}
