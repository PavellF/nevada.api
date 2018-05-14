package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> { 

	
}
