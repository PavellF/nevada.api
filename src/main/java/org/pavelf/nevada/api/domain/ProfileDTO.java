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

/**
 * Represents application profile.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class ProfileDTO {

	private int id;
	
	@NotNull
	@Size(min = 1, max = 128)
	private String username;
	
	@NotNull
	@Size(min = 1, max = 1024)
	private char[] password;
	
	private Instant signDate;
	
	@NotNull
	@javax.validation.constraints.Email
	private String email;
	
	private MessageDTO about;
	
	private PhotoDTO picture;
	
	private int pictureId;
	
	private int popularity;
	
	private int rating;
	
	private String preferences;
	
	private int personId; 
	
	private PersonDTO person;
	
	public static ProfileDTO of(int id, String username, Instant signDate, String email,
			MessageDTO about, PhotoDTO picture, int pictureId, int popularity,
			int rating, String preferences, int personId, PersonDTO person) {
		ProfileDTO thisProfile = new ProfileDTO();
		thisProfile.id = id;
		thisProfile.username = username;
		thisProfile.signDate = signDate;
		thisProfile.email = email;
		thisProfile.about = about;
		thisProfile.picture = picture;
		thisProfile.pictureId = pictureId;
		thisProfile.popularity = popularity;
		thisProfile.rating = rating;
		thisProfile.preferences = preferences;
		thisProfile.personId = personId;
		thisProfile.person = person;
		return thisProfile;
	}
	
	private ProfileDTO() { }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Profile [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", signDate=");
		builder.append(signDate);
		builder.append(", email=");
		builder.append(email);
		builder.append(", about=");
		builder.append(about);
		builder.append(", picture=");
		builder.append(picture);
		builder.append(", pictureId=");
		builder.append(pictureId);
		builder.append(", popularity=");
		builder.append(popularity);
		builder.append(", rating=");
		builder.append(rating);
		builder.append(", preferences=");
		builder.append(preferences);
		builder.append(", personId=");
		builder.append(personId);
		builder.append(", person=");
		builder.append(person);
		builder.append("]");
		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public char[] getPassword() {
		return password;
	}

	public Instant getSignDate() {
		return signDate;
	}

	public String getEmail() {
		return email;
	}

	public MessageDTO getAbout() {
		return about;
	}

	public PhotoDTO getPicture() {
		return picture;
	}

	public int getPictureId() {
		return pictureId;
	}

	public int getPopularity() {
		return popularity;
	}

	public int getRating() {
		return rating;
	}

	public String getPreferences() {
		return preferences;
	}

	public int getPersonId() {
		return personId;
	}

	public PersonDTO getPerson() {
		return person;
	}

	
	
	

	
}
