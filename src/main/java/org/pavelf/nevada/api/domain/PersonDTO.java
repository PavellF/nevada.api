package org.pavelf.nevada.api.domain;

/**
 * Represents real-world person.
 * @since 1.0
 * @author Pavel F.
 * */
public class PersonDTO {

	private Integer id;
	private String fullName;
	private String location;
	private String gender;

	private PersonDTO() { }

	private PersonDTO(Builder builder) {
		this.id = builder.id;
		this.fullName = builder.fullName;
		this.location = builder.location;
		this.gender = builder.gender;
	}
	
	public Integer getId() {
		return id;
	}
	public String getFullName() {
		return fullName;
	}
	public String getLocation() {
		return location;
	}
	public String getGender() {
		return gender;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonDTO [id=");
		builder.append(id);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append(", location=");
		builder.append(location);
		builder.append(", gender=");
		builder.append(gender);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Creates builder to build {@link PersonDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link PersonDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private String fullName;
		private String location;
		private String gender;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withFullName(String fullName) {
			this.fullName = fullName;
			return this;
		}

		public Builder withLocation(String location) {
			this.location = location;
			return this;
		}

		public Builder withGender(String gender) {
			this.gender = gender;
			return this;
		}

		public PersonDTO build() {
			return new PersonDTO(this);
		}
	}
	
}
