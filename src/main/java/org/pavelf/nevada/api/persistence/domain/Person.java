package org.pavelf.nevada.api.persistence.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="people")
public class Person implements Serializable {

	private static final long serialVersionUID = -5646355408258534431L;

	@Column(name = "profile_id")
	@Id
	private int id;
	
	@Column(name = "full_name")
	@Size(min = 1, max = 128)
	private String fullName;
	
	@Column(name = "location")
	@Size(min = 1, max = 128)
	private String location;
	
	@Column(name = "gender")
	@Size(min = 1, max = 128)
	private String gender;
	
	@OneToOne(targetEntity = Profile.class, fetch=FetchType.LAZY,
			cascade = { })
	@JoinColumn(name="profile_id", updatable = false, insertable = false)
	private Profile profile;
	
	public Person() { }
	
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
/*
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
*/

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
