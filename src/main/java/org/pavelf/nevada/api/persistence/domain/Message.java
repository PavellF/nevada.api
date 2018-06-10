package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="messages")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional=false)
	@JoinColumn(name ="author", insertable = false, updatable = false)
	@NotNull
	private Profile author;
	
	@Column(name ="author")
	private int authorId;
	
	@JoinTable(name = "liked_messages", joinColumns = { @JoinColumn(name = "liked_message_id") }, 
		    inverseJoinColumns = { @JoinColumn(name = "like_id")} )
	@OneToMany(fetch=FetchType.LAZY, 
	cascade = {CascadeType.ALL},
	 orphanRemoval = true	)
	private Set<Like> likes;

	@JoinTable(name = "message_has_photo", joinColumns = { @JoinColumn(name = "message_id") }, 
		    inverseJoinColumns = { @JoinColumn(name = "photo_id")} )
	@OneToMany(fetch=FetchType.LAZY, 
	cascade = { },
	 orphanRemoval = false	)
	private Set<Photo> photos;
	
	@JoinTable(name = "message_has_tag", joinColumns = { @JoinColumn(name = "message_id") }, 
		    inverseJoinColumns = { @JoinColumn(name = "tag_name")} )
	@OneToMany(fetch=FetchType.LAZY, 
	cascade = { },
	 orphanRemoval = false	)
	private Set<Tag> tags;
	
	@Column(name="date")
	@NotNull
	@Past
	private Instant date;
	
	@Column(name="reply_to")
	private Integer replyTo;
	
	@Column(name="last_change")
	private Instant lastChange;
	
	@NotNull
	@Size(min = 1, max = 16384)
	@Column(name="content")
	private String content;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = { })
	@JoinColumn(name = "reply_to")
	private List<Message> replies;
	
	@Column(name="rating")
	private int rating;
	
	@Column(name="priority")
	private short priority;
	
	@Column(name="archived")
	private boolean archived;
	
	public Message() { }
	
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

	public Set<Like> getLikes() {
		return likes;
	}

	public void setLikes(Set<Like> likes) {
		this.likes = likes;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Instant getLastChange() {
		return lastChange;
	}

	public void setLastChange(Instant lastChange) {
		this.lastChange = lastChange;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Message> getReplies() {
		return replies;
	}

	public void setReplies(List<Message> replies) {
		this.replies = replies;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Integer getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	
}
