package org.pavelf.nevada.api.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="albums")
public class Album {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "owner_id")
	private Profile ownerId;
	
	@Column(name="title")
	@Size(min=3, max=64)
	@NotNull
	private String title;
	
	@Column(name="visibility_level")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility visibility;
	
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

	public Profile getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Profile ownerId) {
		this.ownerId = ownerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	
}
