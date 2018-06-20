package org.pavelf.nevada.api.domain;

import java.time.Instant;
import org.pavelf.nevada.api.persistence.domain.Visibility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Represents stream post that users usually create.
 * @author Pavel F.
 * @since 1.0
 * */
@JsonInclude(Include.NON_NULL)
public class StreamPostDTO {

	private Integer id;
	private Integer authorId;
	private Instant date;
	private String content;
	private Integer rating;
	private Integer popularity;
	private Short priority;
	private Visibility visibility;
	private Instant lastChange;
	private Visibility commentable;
	private Short userRating;
	private Integer associatedProfile;

	private StreamPostDTO(Builder builder) {
		this.id = builder.id;
		this.authorId = builder.authorId;
		this.date = builder.date;
		this.content = builder.content;
		this.rating = builder.rating;
		this.popularity = builder.popularity;
		this.priority = builder.priority;
		this.visibility = builder.visibility;
		this.lastChange = builder.lastChange;
		this.commentable = builder.commentable;
		this.userRating = builder.userRating;
		this.associatedProfile = builder.associatedProfile;
	}

	private StreamPostDTO() { }

	public Integer getId() {
		return id;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public Instant getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public Integer getRating() {
		return rating;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public Short getPriority() {
		return priority;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public Instant getLastChange() {
		return lastChange;
	}

	public Visibility getCommentable() {
		return commentable;
	}

	public Short getUserRating() {
		return userRating;
	}

	public Integer getAssociatedProfile() {
		return associatedProfile;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StreamPostDTO [id=");
		builder.append(id);
		builder.append(", authorId=");
		builder.append(authorId);
		builder.append(", date=");
		builder.append(date);
		builder.append(", content=");
		builder.append(content);
		builder.append(", rating=");
		builder.append(rating);
		builder.append(", popularity=");
		builder.append(popularity);
		builder.append(", priority=");
		builder.append(priority);
		builder.append(", visibility=");
		builder.append(visibility);
		builder.append(", lastChange=");
		builder.append(lastChange);
		builder.append(", commentable=");
		builder.append(commentable);
		builder.append(", userRating=");
		builder.append(userRating);
		builder.append(", associatedProfile=");
		builder.append(associatedProfile);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link StreamPostDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link StreamPostDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer authorId;
		private Instant date;
		private String content;
		private Integer rating;
		private Integer popularity;
		private Short priority;
		private Visibility visibility;
		private Instant lastChange;
		private Visibility commentable;
		private Short userRating;
		private Integer associatedProfile;

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

		public Builder withContent(String content) {
			this.content = content;
			return this;
		}

		public Builder withRating(Integer rating) {
			this.rating = rating;
			return this;
		}

		public Builder withPopularity(Integer popularity) {
			this.popularity = popularity;
			return this;
		}

		public Builder withPriority(Short priority) {
			this.priority = priority;
			return this;
		}

		public Builder withVisibility(Visibility visibility) {
			this.visibility = visibility;
			return this;
		}

		public Builder withLastChange(Instant lastChange) {
			this.lastChange = lastChange;
			return this;
		}

		public Builder withCommentable(Visibility commentable) {
			this.commentable = commentable;
			return this;
		}

		public Builder withUserRating(Short userRating) {
			this.userRating = userRating;
			return this;
		}

		public Builder withAssociatedProfile(Integer associatedProfile) {
			this.associatedProfile = associatedProfile;
			return this;
		}

		public StreamPostDTO build() {
			return new StreamPostDTO(this);
		}
	}
	
	
}
