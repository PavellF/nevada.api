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
@Table(name="followers")
public class Follower {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "follower_id", insertable = false, updatable = false)
	private Profile follower;
	
	@Column(name = "follower_id")
	private int followerId;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = false)
	@JoinColumn(name = "followed_id", insertable = false, updatable = false)
	private Profile followed;
	
	@Column(name = "followed_id")
	private int followedId;
	
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

	public Profile getFollower() {
		return follower;
	}

	public void setFollower(Profile follower) {
		this.follower = follower;
	}

	public int getFollowerId() {
		return followerId;
	}

	public void setFollowerId(int followerId) {
		this.followerId = followerId;
	}

	public Profile getFollowed() {
		return followed;
	}

	public void setFollowed(Profile followed) {
		this.followed = followed;
	}

	public int getFollowedId() {
		return followedId;
	}

	public void setFollowedId(int followedId) {
		this.followedId = followedId;
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
