package org.pavelf.nevada.api.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.pavelf.nevada.api.Application.*;
import javax.annotation.Generated;

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
	private transient Version version;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
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
		builder.append(", version=");
		builder.append(version);
		builder.append("]");
		return builder.toString();
	}

	
}
