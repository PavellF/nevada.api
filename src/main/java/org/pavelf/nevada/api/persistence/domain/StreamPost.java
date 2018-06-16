package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

/**
 * ORM mapping {@code @Entity} that represents stream post.
 * @author Pavel F.
 * @since 1.0
 * */
@Entity
@Table(name="stream_post")
public class StreamPost {

	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinTable(name = "like_stream_post", 
		joinColumns = @JoinColumn(name = "stream_post_id"), 
		inverseJoinColumns = @JoinColumn(name = "like_id"))
	private Like currentUserLike;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinColumn(name = "author", insertable = false, updatable = false)
	private Profile author;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinTable(name = "profile_has_stream_post", 
		joinColumns = @JoinColumn(name = "stream_post_id"), 
		inverseJoinColumns = @JoinColumn(name = "profile_id"))
	private Profile associatedProfile;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional = true)
	@JoinTable(name = "stream_post_has_tag", 
			joinColumns = @JoinColumn(name = "stream_post_id"), 
			inverseJoinColumns = @JoinColumn(name = "tag"))
	private Tag associatedTag;
	
	@Column(name = "author")
	private int authorId;
	
	@Column(name="date")
	@NotNull
	@Past
	private Instant date;
	
	@Column(name = "content")
	@Size(min=1, max=16384)
	@NotNull
	private String content;
	
	@Column(name = "rating")
	private int rating;
	
	@Column(name  = "popularity")
	private int popularity;
	
	@Column(name  = "priority")
	private short priority;
	
	@Column(name="visibility")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility visibility;
	
	@Column(name="commentable")
	@NotNull
	@Enumerated(javax.persistence.EnumType.STRING)
	private Visibility commentable; 
	
	@Column(name="last_change")
	private Instant lastChange;
	
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

	public Profile getAuthor() {
		return author;
	}

	public void setAuthor(Profile author) {
		this.author = author;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public Instant getLastChange() {
		return lastChange;
	}

	public void setLastChange(Instant lastChange) {
		this.lastChange = lastChange;
	}

	public Tag getAssociatedTag() {
		return associatedTag;
	}

	public void setAssociatedTag(Tag associatedTag) {
		this.associatedTag = associatedTag;
	}

	public Visibility getCommentable() {
		return commentable;
	}

	public void setCommentable(Visibility commentable) {
		this.commentable = commentable;
	}

	public Profile getAssociatedProfile() {
		return associatedProfile;
	}

	public void setAssociatedProfile(Profile associatedProfile) {
		this.associatedProfile = associatedProfile;
	}

	public Like getCurrentUserLike() {
		return currentUserLike;
	}

	public void setCurrentUserLike(Like currentUserLike) {
		this.currentUserLike = currentUserLike;
	}

}
