package org.pavelf.nevada.api.persistence.domain;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Entity
@Table(name="messages")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { }, optional=false)
	@JoinColumn(name ="author", insertable = false, updatable = false)
	private Profile author;
	
	@Column(name ="author")
	private int authorId;
	
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
	
	@Column(name="rating")
	private int rating;
	
	@Column(name="priority")
	private short priority;
	
	@Column(name="archived")
	private boolean archived;
	
	@Column(name="stream_post_message")
	private Integer associatedStreamPost;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Integer getAssociatedStreamPost() {
		return associatedStreamPost;
	}

	public void setAssociatedStreamPost(Integer associatedStreamPost) {
		this.associatedStreamPost = associatedStreamPost;
	}

	
}
