package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import javax.annotation.Generated;

/**
 * Represents text message with attachments.
 * @since 1.0
 * @author Pavel F.
 * */
@JsonInclude(Include.NON_NULL)
public class MessageDTO {

	private Integer id;
	private Integer authorId;
	private Instant date;
	private Instant lastChange;
	private Integer rating;
	private Short priority;
	private Boolean archived;
	private String content;
	private Set<Integer> likeIds;
	private List<Integer> photoIds;
	private Boolean canLike;
	private List<Integer> repliesIds;
	private Integer replyTo;

	private MessageDTO() { }
	
	private MessageDTO(Builder builder) {
		this.id = builder.id;
		this.authorId = builder.authorId;
		this.date = builder.date;
		this.lastChange = builder.lastChange;
		this.rating = builder.rating;
		this.priority = builder.priority;
		this.archived = builder.archived;
		this.content = builder.content;
		this.likeIds = builder.likeIds;
		this.photoIds = builder.photoIds;
		this.canLike = builder.canLike;
		this.repliesIds = builder.repliesIds;
		this.replyTo = builder.replyTo;
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getAuthorId() {
		return authorId;
	}
	public Instant getDate() {
		return date;
	}
	public Instant getLastChange() {
		return lastChange;
	}
	public Integer getRating() {
		return rating;
	}
	public Short getPriority() {
		return priority;
	}
	public Boolean getArchived() {
		return archived;
	}
	public String getContent() {
		return content;
	}
	public Set<Integer> getLikeIds() {
		return likeIds;
	}
	public List<Integer> getPhotoIds() {
		return photoIds;
	}
	public Boolean getCanLike() {
		return canLike;
	}
	public List<Integer> getRepliesIds() {
		return repliesIds;
	}
	public Integer getReplyTo() {
		return replyTo;
	}
	
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
		builder.append(", likeIds=");
		builder.append(likeIds);
		builder.append(", photoIds=");
		builder.append(photoIds);
		builder.append(", canLike=");
		builder.append(canLike);
		builder.append(", repliesIds=");
		builder.append(repliesIds);
		builder.append(", replyTo=");
		builder.append(replyTo);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Creates builder to build {@link MessageDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Builder to build {@link MessageDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer authorId;
		private Instant date;
		private Instant lastChange;
		private Integer rating;
		private Short priority;
		private Boolean archived;
		private String content;
		private Set<Integer> likeIds = Collections.emptySet();
		private List<Integer> photoIds = Collections.emptyList();
		private Boolean canLike;
		private List<Integer> repliesIds = Collections.emptyList();
		private Integer replyTo;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withAuthorId(Integer authorId) {
			this.authorId = authorId;
			return this;
		}

		public Builder withDate(Instant date) {
			this.date = date;
			return this;
		}

		public Builder withLastChange(Instant lastChange) {
			this.lastChange = lastChange;
			return this;
		}

		public Builder withRating(Integer rating) {
			this.rating = rating;
			return this;
		}

		public Builder withPriority(Short priority) {
			this.priority = priority;
			return this;
		}

		public Builder withArchived(Boolean archived) {
			this.archived = archived;
			return this;
		}

		public Builder withContent(String content) {
			this.content = content;
			return this;
		}

		public Builder withLikeIds(Set<Integer> likeIds) {
			this.likeIds = likeIds;
			return this;
		}

		public Builder withPhotoIds(List<Integer> photoIds) {
			this.photoIds = photoIds;
			return this;
		}

		public Builder withCanLike(Boolean canLike) {
			this.canLike = canLike;
			return this;
		}

		public Builder withRepliesIds(List<Integer> repliesIds) {
			this.repliesIds = repliesIds;
			return this;
		}

		public Builder withReplyTo(Integer replyTo) {
			this.replyTo = replyTo;
			return this;
		}

		public MessageDTO build() {
			return new MessageDTO(this);
		}
	}

	
}
