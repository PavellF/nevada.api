package org.pavelf.nevada.api.persistence.repository;

import java.util.Set;

import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Integer> { 

	//@Query("SELECT li.like_id FROM liked_messages AS li WHERE li.liked_message_id = ?1")
	//public Set<Integer> getAllLikesForMessage(int id);
	
}
