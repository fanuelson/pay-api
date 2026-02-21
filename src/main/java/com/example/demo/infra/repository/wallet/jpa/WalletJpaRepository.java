package com.example.demo.infra.repository.wallet.jpa;

import com.example.demo.infra.repository.wallet.jpa.entities.WalletEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import java.util.Optional;

public interface WalletJpaRepository extends JpaRepositoryImplementation<WalletEntity, Long> {
  Optional<WalletEntity> findByUserId(Long userId);
}