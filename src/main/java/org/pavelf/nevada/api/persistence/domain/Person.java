package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * ORM {@code @Entity} that represents person.
 * @author Pavel F.
 * @since 1.0
 * */
@Entity
@Table(name="people")
public class Person {

	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;	
	
	@Column(name = "profile_id")
	private Integer associatedProfileId;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssociatedProfileId() {
		return associatedProfileId;
	}

	public void setAssociatedProfileId(Integer associatedProfileId) {
		this.associatedProfileId = associatedProfileId;
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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}


}
