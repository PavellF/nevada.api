package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.StreamPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamPostTagRepository
	extends JpaRepository<StreamPostTag, Integer> {

}
