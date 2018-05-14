package org.pavelf.nevada.api.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.pavelf.nevada.api.Application.*;

/**
 * Represents real-world person.
 * @since 1.0
 * @author Pavel F.
 * */
public final class PersonDTO {

	private int id;
	
	@Size(min = 1, max = 128)
	private String fullName;
	
	@Column(name = "location")
	@Size(min = 1, max = 128)
	private String location;
	
	@Column(name = "gender")
	@Size(min = 1, max = 64)
	private String gender;

	private PersonDTO() { }

	public int getId() {
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

	public static PersonDTO of(int id, String fullName, String location, String gender) {
		PersonDTO person = new PersonDTO();
		person.setId(id);
		person.setFullName(fullName);
		person.setLocation(location);
		person.setGender(gender);
		return person;
	}
	
	public static PersonDTO empty() {
		PersonDTO person = new PersonDTO();
		return person;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Person [id=");
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDTO other = (PersonDTO) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id != other.id)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	private void setId(int id) {
		this.id = id;
	}

	private void setFullName(String fullName) {
		this.fullName = fullName;
	}

	private void setLocation(String location) {
		this.location = location;
	}

	private void setGender(String gender) {
		this.gender = gender;
	}
	
}
