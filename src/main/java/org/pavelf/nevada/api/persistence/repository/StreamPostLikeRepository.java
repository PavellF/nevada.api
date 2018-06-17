package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.StreamPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamPostLikeRepository
	extends JpaRepository<StreamPostLike, Integer> {

}
