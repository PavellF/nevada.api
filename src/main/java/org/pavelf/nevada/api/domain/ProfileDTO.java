package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.domain.PersonDTO;
import org.pavelf.nevada.api.persistence.domain.Photo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.annotation.Generated;

/**
 * Represents application profile.
 * @since 1.0
 * @author Pavel F.
 * */


/*
 * controller : services, validation
 * service: repository
 * */
@JsonInclude(Include.NON_NULL)

public class ProfileDTO {

	private Integer id;
	
	private String username;
	
	private char[] password;
	
	private Instant signDate;
	
	private String email;
	
	private MessageDTO about;
	
	private PhotoDTO picture;
	
	private Integer pictureId;
	
	private Integer popularity;
	
	private Integer rating;
	
	private Integer personId; 
	
	private PersonDTO person;
	
	private Integer aboutId;
	
	private transient Version version;
	
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

	public MessageDTO getAbout() {
		return about;
	}

	public void setAbout(MessageDTO about) {
		this.about = about;
	}

	public PhotoDTO getPicture() {
		return picture;
	}

	public void setPicture(PhotoDTO picture) {
		this.picture = picture;
	}

	public Integer getPictureId() {
		return pictureId;
	}

	public void setPictureId(Integer pictureId) {
		this.pictureId = pictureId;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public PersonDTO getPerson() {
		return person;
	}

	public void setPerson(PersonDTO person) {
		this.person = person;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Integer getAboutId() {
		return aboutId;
	}

	public void setAboutId(Integer aboutId) {
		this.aboutId = aboutId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProfileDTO [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", signDate=");
		builder.append(signDate);
		builder.append(", email=");
		builder.append(email);
		builder.append(", pictureId=");
		builder.append(pictureId);
		builder.append(", popularity=");
		builder.append(popularity);
		builder.append(", rating=");
		builder.append(rating);
		builder.append(", personId=");
		builder.append(personId);
		builder.append(", aboutId=");
		builder.append(aboutId);
		builder.append("]");
		return builder.toString();
	}

	
	
}
