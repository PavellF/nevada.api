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
@JsonInclude(Include.NON_NULL)
public class ProfileDTO {

	private Integer id;
	
	private String username;
	
	private char[] password;
	
	private char[] oldPassword;
	
	private Instant signDate;
	
	private String email;
	
	private Integer pictureId;
	
	private Integer popularity;
	
	private Integer rating;
	
	private Integer aboutId;
	
	private Instant suspendedUntil;

	private ProfileDTO(Builder builder) {
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;
		this.oldPassword = builder.oldPassword;
		this.signDate = builder.signDate;
		this.email = builder.email;
		this.pictureId = builder.pictureId;
		this.popularity = builder.popularity;
		this.rating = builder.rating;
		this.aboutId = builder.aboutId;
		this.suspendedUntil = builder.suspendedUntil;
	}

	private ProfileDTO() { }

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public char[] getPassword() {
		return password;
	}

	public char[] getOldPassword() {
		return oldPassword;
	}

	public Instant getSignDate() {
		return signDate;
	}

	public String getEmail() {
		return email;
	}

	public Integer getPictureId() {
		return pictureId;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public Integer getRating() {
		return rating;
	}

	public Integer getAboutId() {
		return aboutId;
	}

	public Instant getSuspendedUntil() {
		return suspendedUntil;
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
		builder.append(", aboutId=");
		builder.append(aboutId);
		builder.append(", suspendedUntil=");
		builder.append(suspendedUntil);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link ProfileDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link ProfileDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private String username;
		private char[] password;
		private char[] oldPassword;
		private Instant signDate;
		private String email;
		private Integer pictureId;
		private Integer popularity;
		private Integer rating;
		private Integer aboutId;
		private Instant suspendedUntil;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder withPassword(char[] password) {
			this.password = password;
			return this;
		}

		public Builder withOldPassword(char[] oldPassword) {
			this.oldPassword = oldPassword;
			return this;
		}

		public Builder withSignDate(Instant signDate) {
			this.signDate = signDate;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withPictureId(Integer pictureId) {
			this.pictureId = pictureId;
			return this;
		}

		public Builder withPopularity(Integer popularity) {
			this.popularity = popularity;
			return this;
		}

		public Builder withRating(Integer rating) {
			this.rating = rating;
			return this;
		}

		public Builder withAboutId(Integer aboutId) {
			this.aboutId = aboutId;
			return this;
		}

		public Builder withSuspendedUntil(Instant suspendedUntil) {
			this.suspendedUntil = suspendedUntil;
			return this;
		}

		public ProfileDTO build() {
			return new ProfileDTO(this);
		}
	}
	
	
}
