package org.pavelf.nevada.api.persistence.repository;


import org.pavelf.nevada.api.persistence.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

}
