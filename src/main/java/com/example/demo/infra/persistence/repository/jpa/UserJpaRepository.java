package com.example.demo.infra.persistence.repository.jpa;

import com.example.demo.infra.persistence.repository.entities.UserEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepositoryImplementation<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findByDocument(String document);
}