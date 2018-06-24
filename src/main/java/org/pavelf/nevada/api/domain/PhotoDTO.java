package org.pavelf.nevada.api.domain;

import java.time.Instant;

import org.pavelf.nevada.api.persistence.domain.Visibility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Describes application specific photo meta data, not image itself.
 * @author Pavel F.
 * @since 1.0
 * */
@JsonInclude(Include.NON_NULL)
public class PhotoDTO {

	private Integer id;
	private Integer ownerId;
	private Instant postDate;
	private String message;
	private String fileName;
	private Visibility visibility;
	private boolean archived;

	private PhotoDTO(Builder builder) {
		this.id = builder.id;
		this.ownerId = builder.ownerId;
		this.postDate = builder.postDate;
		this.message = builder.message;
		this.fileName = builder.fileName;
		this.visibility = builder.visibility;
		this.archived = builder.archived;
	}
	
	private PhotoDTO() { }

	public Integer getId() {
		return id;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public Instant getPostDate() {
		return postDate;
	}

	public String getMessage() {
		return message;
	}

	public String getFileName() {
		return fileName;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public boolean isArchived() {
		return archived;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PhotoDTO [id=");
		builder.append(id);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append(", postDate=");
		builder.append(postDate);
		builder.append(", message=");
		builder.append(message);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", visibility=");
		builder.append(visibility);
		builder.append(", archived=");
		builder.append(archived);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Creates builder to build {@link PhotoDTO}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link PhotoDTO}.
	 */
	public static final class Builder {
		private Integer id;
		private Integer ownerId;
		private Instant postDate;
		private String message;
		private String fileName;
		private Visibility visibility;
		private boolean archived;

		private Builder() {
		}

		public Builder withId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder withOwnerId(Integer ownerId) {
			this.ownerId = ownerId;
			return this;
		}

		public Builder withPostDate(Instant postDate) {
			this.postDate = postDate;
			return this;
		}

		public Builder withMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder withFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public Builder withVisibility(Visibility visibility) {
			this.visibility = visibility;
			return this;
		}

		public Builder withArchived(boolean archived) {
			this.archived = archived;
			return this;
		}

		public PhotoDTO build() {
			return new PhotoDTO(this);
		}
	}

	
}
