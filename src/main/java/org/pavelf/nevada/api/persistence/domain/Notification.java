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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="notifications")
public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="when")
	@NotNull
	@Past
	private Instant postDate;
	
	@Column(name="expired")
	private boolean expired;
	
	@Column(name = "type")
	@Size(min = 4, max = 64)
	@NotNull
	private String type;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "belongs_to", insertable = false, updatable = false)
	private Profile belongsTo; 
	
	@Column(name = "belongs_to")
	private int belongsToId;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@NotNull
	@JoinColumn(name = "issued_by", insertable = false, updatable = false)
	private Profile issuedBy;
	
	@Column(name = "issued_by")
	private int issuedById;
	
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

	public Instant getPostDate() {
		return postDate;
	}

	public void setPostDate(Instant postDate) {
		this.postDate = postDate;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Profile getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(Profile belongsTo) {
		this.belongsTo = belongsTo;
	}

	public int getBelongsToId() {
		return belongsToId;
	}

	public void setBelongsToId(int belongsToId) {
		this.belongsToId = belongsToId;
	}

	public Profile getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(Profile issuedBy) {
		this.issuedBy = issuedBy;
	}

	public int getIssuedById() {
		return issuedById;
	}

	public void setIssuedById(int issuedById) {
		this.issuedById = issuedById;
	}
	
	
}
