package com.example.demo.infra.persistence.repository.jpa;

import com.example.demo.infra.persistence.repository.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {

  Optional<WalletEntity> findByUserId(Long userId);
  boolean existsByUserId(Long userId);
}