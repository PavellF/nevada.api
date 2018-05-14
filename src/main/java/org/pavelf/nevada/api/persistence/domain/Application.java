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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="applications")
public class Application {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="title")
	@Size(min = 3, max = 128)
	private String title;
	
	@Column(name="suspended_until")
	private Instant suspendedUntil;
	
	@Column(name="since")
	@NotNull
	@Past
	private Instant since;
	
	@Column(name="access_key")
	@Size(min = 1, max = 1024)
	private String accessKey;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "belongs_to_profile")
	private Profile profile; 
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Instant getSuspendedUntil() {
		return suspendedUntil;
	}

	public void setSuspendedUntil(Instant suspendedUntil) {
		this.suspendedUntil = suspendedUntil;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Instant getSince() {
		return since;
	}

	public void setSince(Instant since) {
		this.since = since;
	}

}
