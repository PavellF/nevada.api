package org.pavelf.nevada.api.domain;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Photo;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.pavelf.nevada.api.persistence.domain.Tag;

public class MessageDTO {

	private int id;
	private ProfileDTO author;
	private Instant date;
	private Instant lastChange;
	private int rating;
	private short priority;
	private boolean archived;
	private String content;
	private Set<Like> likes;
	private Set<PhotoDTO> photos;
	private Set<Tag> tags;
	private List<Message> replies;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
