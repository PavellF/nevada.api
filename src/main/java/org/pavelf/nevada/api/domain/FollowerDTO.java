package org.pavelf.nevada.api.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents user profile follower.
 * @author Pavel F.
 * @since 1.0
 **/
@JsonInclude(Include.NON_NULL)
public class FollowerDTO {

	private Integer id;
	private Integer followerId;
	private Integer followedId;
	private Instant since;
	private Boolean mutual;

	private FollowerDTO() { }
	
	private FollowerDTO(Builder builder) {
		this.id = builder.id;
		this.followerId = builder.followerId;
		this.followedId = builder.followedId;
		this.since = builder.since;
		this.mutual = builder.mutual;
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getFollowerId() {
		return followerId;
	}
	public Integer getFollowedId() {
		return followedId;
	}
	public Instant getSince() {
		return since;
	}
	public Boolean getMutual() {
		return mutual;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FollowerDTO [id=");
		builder.append(id);
		builder.append(", followerId=");
		builder.append(followerId);
		builder.append(", followedId=");
		builder.append(followedId);
		builder.append(", since=");
		builder.append(since);
		builder.append(", mutual=");
		builder.append(mutual);
		builder.append("]");
		return builder.toString();
	}
	/**
	 * Creates builder to build {@link FollowerDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link FollowerDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer followerId;
		private Integer followedId;
		private Instant since;
		private Boolean mutual;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withFollowerId(Integer followerId) {
			this.followerId = followerId;
			return this;
		}

		public Builder withFollowedId(Integer followedId) {
			this.followedId = followedId;
			return this;
		}

		public Builder withSince(Instant since) {
			this.since = since;
			return this;
		}

		public Builder withMutual(Boolean mutual) {
			this.mutual = mutual;
			return this;
		}

		public FollowerDTO build() {
			return new FollowerDTO(this);
		}
	}
	
}
