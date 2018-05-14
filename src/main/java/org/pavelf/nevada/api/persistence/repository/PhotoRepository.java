package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}
