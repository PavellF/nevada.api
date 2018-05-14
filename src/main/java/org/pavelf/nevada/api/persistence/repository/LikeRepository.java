package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {

}
