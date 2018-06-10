package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Visibility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.annotation.Generated;
import java.util.Collections;
import java.util.Iterator;

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
