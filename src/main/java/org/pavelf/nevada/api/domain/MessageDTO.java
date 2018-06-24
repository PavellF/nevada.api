package org.pavelf.nevada.api.domain;

import java.time.Instant;
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
	private Integer authorId;
	private Instant date;
	private Instant lastChange;
	private Integer rating;
	private Short priority;
	private Boolean archived;
	private String content;
	private Integer replyTo;
	private Integer streamPostId;
	private Integer countOfReplies;
	private Short currentUserRating;
	private Set<String> tags;
	private Set<Integer> images;

	private MessageDTO(Builder builder) {
		this.id = builder.id;
		this.authorId = builder.authorId;
		this.date = builder.date;
		this.lastChange = builder.lastChange;
		this.rating = builder.rating;
		this.priority = builder.priority;
		this.archived = builder.archived;
		this.content = builder.content;
		this.replyTo = builder.replyTo;
		this.streamPostId = builder.streamPostId;
		this.countOfReplies = builder.countOfReplies;
		this.currentUserRating = builder.currentUserRating;
		this.tags = builder.tags;
		this.images = builder.images;
	}

	private MessageDTO() { }

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

	public Integer getReplyTo() {
		return replyTo;
	}

	public Integer getStreamPostId() {
		return streamPostId;
	}

	public Integer getCountOfReplies() {
		return countOfReplies;
	}

	public Short getCurrentUserRating() {
		return currentUserRating;
	}

	public Set<String> getTags() {
		return tags;
	}

	public Set<Integer> getImages() {
		return images;
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
		builder.append(", replyTo=");
		builder.append(replyTo);
		builder.append(", streamPostId=");
		builder.append(streamPostId);
		builder.append(", countOfReplies=");
		builder.append(countOfReplies);
		builder.append(", currentUserRating=");
		builder.append(currentUserRating);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", images=");
		builder.append(images);
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
		private Integer replyTo;
		private Integer streamPostId;
		private Integer countOfReplies;
		private Short currentUserRating;
		private Set<String> tags = Collections.emptySet();
		private Set<Integer> images = Collections.emptySet();

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

		public Builder withReplyTo(Integer replyTo) {
			this.replyTo = replyTo;
			return this;
		}

		public Builder withStreamPostId(Integer streamPostId) {
			this.streamPostId = streamPostId;
			return this;
		}

		public Builder withCountOfReplies(Integer countOfReplies) {
			this.countOfReplies = countOfReplies;
			return this;
		}

		public Builder withCurrentUserRating(Short currentUserRating) {
			this.currentUserRating = currentUserRating;
			return this;
		}

		public Builder withTags(Set<String> tags) {
			this.tags = tags;
			return this;
		}

		public Builder withImages(Set<Integer> images) {
			this.images = images;
			return this;
		}

		public MessageDTO build() {
			return new MessageDTO(this);
		}
	}

}
