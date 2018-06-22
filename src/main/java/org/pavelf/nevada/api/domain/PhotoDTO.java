package org.pavelf.nevada.api.domain;

import java.time.Instant;
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
	private byte[] original;
	
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

	public byte[] getOriginal() {
		return original;
	}
	
}
