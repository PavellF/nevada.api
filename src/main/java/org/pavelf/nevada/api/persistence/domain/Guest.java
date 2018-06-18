package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
@Table(name="guests")
public class Guest {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "who", insertable = false, updatable = false)
	private Profile who; 
	
	@Column(name = "who")
	private int whoId;
	
	@Column(name = "hidden")
	private boolean hidden;
	
	@Column(name="when")
	@NotNull
	@Past
	private Instant when;
	
	@Column(name = "to_profile")
	private Integer toProfile;
	
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

	public Profile getWho() {
		return who;
	}

	public void setWho(Profile who) {
		this.who = who;
	}

	public int getWhoId() {
		return whoId;
	}

	public void setWhoId(int whoId) {
		this.whoId = whoId;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Instant getWhen() {
		return when;
	}

	public void setWhen(Instant when) {
		this.when = when;
	}

	public Integer getToProfile() {
		return toProfile;
	}

	public void setToProfile(Integer toProfile) {
		this.toProfile = toProfile;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
