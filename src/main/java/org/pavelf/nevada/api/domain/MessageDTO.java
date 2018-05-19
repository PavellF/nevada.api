package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;

/**
 * Represents text message with attachments.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class MessageDTO {

	private Integer id;
	private ProfileDTO author;
	private Integer authorId;
	private Instant date;
	private Instant lastChange;
	private Integer rating;
	private Short priority;
	private Boolean archived;
	private String content;
	private Set<LikeDTO> likes;
	private Set<PhotoDTO> photos;
	private Set<TagDTO> tags;
	private List<MessageDTO> replies;
	private Integer replyTo;
	private Version version;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessageDTO [id=");
		builder.append(id);
		builder.append(", authorId=");
		builder.append(authorId);
		builder.append(", date=");
		builder.append(date);
		builder.append(", lastChange=");
		builder.append(lastChange);
		builder.append(", rating=");
		builder.append(rating);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", archived=");
		builder.append(archived);
		builder.append(", content=");
		builder.append(content);
		builder.append(", replies=");
		builder.append(replies);
		builder.append(", replyTo=");
		builder.append(replyTo);
		builder.append("]");
		return builder.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProfileDTO getAuthor() {
		return author;
	}

	public void setAuthor(ProfileDTO author) {
		this.author = author;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
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

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Short getPriority() {
		return priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<LikeDTO> getLikes() {
		return likes;
	}

	public void setLikes(Set<LikeDTO> likes) {
		this.likes = likes;
	}

	public Set<PhotoDTO> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<PhotoDTO> photos) {
		this.photos = photos;
	}

	public Set<TagDTO> getTags() {
		return tags;
	}

	public void setTags(Set<TagDTO> tags) {
		this.tags = tags;
	}

	public List<MessageDTO> getReplies() {
		return replies;
	}

	public void setReplies(List<MessageDTO> replies) {
		this.replies = replies;
	}

	public Integer getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Integer replyTo) {
		this.replyTo = replyTo;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
	
}
