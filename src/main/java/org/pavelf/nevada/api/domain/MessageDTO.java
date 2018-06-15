package org.pavelf.nevada.api.domain;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private Boolean isLiked;
	private Integer replyTo;
	private Owner ownerType;
	private Integer ownerId;
	private Destination destinationType;
	private Integer destinationId;

	private MessageDTO(Builder builder) {
		this.id = builder.id;
		this.authorId = builder.authorId;
		this.date = builder.date;
		this.lastChange = builder.lastChange;
		this.rating = builder.rating;
		this.priority = builder.priority;
		this.archived = builder.archived;
		this.content = builder.content;
		this.isLiked = builder.isLiked;
		this.replyTo = builder.replyTo;
		this.ownerType = builder.ownerType;
		this.ownerId = builder.ownerId;
		this.destinationType = builder.destinationType;
		this.destinationId = builder.destinationId;
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

	public Boolean getIsLiked() {
		return isLiked;
	}

	public Integer getReplyTo() {
		return replyTo;
	}

	public Owner getOwnerType() {
		return ownerType;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public Destination getDestinationType() {
		return destinationType;
	}

	public Integer getDestinationId() {
		return destinationId;
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
		builder.append(", isLiked=");
		builder.append(isLiked);
		builder.append(", replyTo=");
		builder.append(replyTo);
		builder.append(", ownerType=");
		builder.append(ownerType);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append(", destinationType=");
		builder.append(destinationType);
		builder.append(", destinationId=");
		builder.append(destinationId);
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
		private Boolean isLiked;
		private Integer replyTo;
		private Owner ownerType;
		private Integer ownerId;
		private Destination destinationType;
		private Integer destinationId;

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

		public Builder withIsLiked(Boolean isLiked) {
			this.isLiked = isLiked;
			return this;
		}

		public Builder withReplyTo(Integer replyTo) {
			this.replyTo = replyTo;
			return this;
		}

		public Builder withOwnerType(Owner ownerType) {
			this.ownerType = ownerType;
			return this;
		}

		public Builder withOwnerId(Integer ownerId) {
			this.ownerId = ownerId;
			return this;
		}

		public Builder withDestinationType(Destination destinationType) {
			this.destinationType = destinationType;
			return this;
		}

		public Builder withDestinationId(Integer destinationId) {
			this.destinationId = destinationId;
			return this;
		}

		public MessageDTO build() {
			return new MessageDTO(this);
		}
	}
	
	
}
