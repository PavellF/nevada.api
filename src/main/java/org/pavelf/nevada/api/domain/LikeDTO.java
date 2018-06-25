package org.pavelf.nevada.api.domain;

import java.time.Instant;

/**
 * Represents user like transfer object.
 * @author Pavel F.
 * @since 1.0
 * */
public class LikeDTO {

	private Integer id;
	private Integer likedMessage;
	private Integer likedStreamPost;
	private Integer likedBy;
	private Instant when;
	private Short rating;

	private LikeDTO() { }
	
	private LikeDTO(Builder builder) {
		this.id = builder.id;
		this.likedMessage = builder.likedMessage;
		this.likedStreamPost = builder.likedStreamPost;
		this.likedBy = builder.likedBy;
		this.when = builder.when;
		this.rating = builder.rating;
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getLikedMessage() {
		return likedMessage;
	}
	public Integer getLikedStreamPost() {
		return likedStreamPost;
	}
	public Integer getLikedBy() {
		return likedBy;
	}
	public Instant getWhen() {
		return when;
	}
	public Short getRating() {
		return rating;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LikeDTO [id=");
		builder.append(id);
		builder.append(", likedMessage=");
		builder.append(likedMessage);
		builder.append(", likedStreamPost=");
		builder.append(likedStreamPost);
		builder.append(", likedBy=");
		builder.append(likedBy);
		builder.append(", when=");
		builder.append(when);
		builder.append(", rating=");
		builder.append(rating);
		builder.append("]");
		return builder.toString();
	}
	/**
	 * Creates builder to build {@link LikeDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link LikeDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer likedMessage;
		private Integer likedStreamPost;
		private Integer likedBy;
		private Instant when;
		private Short rating;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withLikedMessage(Integer likedMessage) {
			this.likedMessage = likedMessage;
			return this;
		}

		public Builder withLikedStreamPost(Integer likedStreamPost) {
			this.likedStreamPost = likedStreamPost;
			return this;
		}

		public Builder withLikedBy(Integer likedBy) {
			this.likedBy = likedBy;
			return this;
		}

		public Builder withWhen(Instant when) {
			this.when = when;
			return this;
		}

		public Builder withRating(Short rating) {
			this.rating = rating;
			return this;
		}

		public LikeDTO build() {
			return new LikeDTO(this);
		}
	}	
	
	
}
