package com.example.demo.infra.repository.user.jpa;

import com.example.demo.infra.repository.user.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepositoryImplementation<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findByDocument(String document);
}