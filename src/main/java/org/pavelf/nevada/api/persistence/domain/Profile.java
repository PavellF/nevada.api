package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="profiles")
public class Profile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="username")
	@NotNull
	@Size(min = 1, max = 128)
	private String username;
	
	@Column(name="password")
	@NotNull
	@Size(min = 1, max = 1024)
	private char[] password;
	
	@Column(name="sign_date")
	@NotNull
	@Past
	private Instant signDate;
	
	@Column(name="email")
	@NotNull
	@javax.validation.constraints.Email
	private String email;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, optional = true)
	@JoinColumn(name = "about")
	private Message about;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.ALL }, optional = true)
	@JoinColumn(name="user_pic")
	private Photo picture;
	
	@Min(0)
	@Column(name="popularity")
	private int popularity;
	
	@Column(name="rating")
	private int rating;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinColumn(name = "belongs_to")
	private Person person; 
	
	public Profile() { }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Photo getPicture() {
		return picture;
	}

	public void setPicture(Photo picture) {
		this.picture = picture;
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

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
