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

@Entity
@Table(name="friendship")
public class Friendship {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "user_id")
	private Profile userId;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "friend_id")
	private Profile friendId;
	
	@Column(name="since")
	private Instant since;
	
	@Column(name = "mutual")
	private boolean mutual;
	
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

	public Profile getUserId() {
		return userId;
	}

	public void setUserId(Profile userId) {
		this.userId = userId;
	}

	public Profile getFriendId() {
		return friendId;
	}

	public void setFriendId(Profile friendId) {
		this.friendId = friendId;
	}

	public Instant getSince() {
		return since;
	}

	public void setSince(Instant since) {
		this.since = since;
	}

	public boolean isMutual() {
		return mutual;
	}

	public void setMutual(boolean mutual) {
		this.mutual = mutual;
	}
	
}
