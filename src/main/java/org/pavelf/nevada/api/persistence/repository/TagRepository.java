package org.pavelf.nevada.api.persistence.repository;

import org.pavelf.nevada.api.persistence.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Defines set of actions with external storage of {@code Tag}.
 * @author Pavel F.
 * @since 1.0
 * */
public interface TagRepository extends JpaRepository<Tag, String> {

}
