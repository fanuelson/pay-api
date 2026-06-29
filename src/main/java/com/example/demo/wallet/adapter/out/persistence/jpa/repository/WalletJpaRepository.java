package com.example.demo.wallet.adapter.out.persistence.jpa.repository;

import com.example.demo.wallet.adapter.out.persistence.jpa.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {

  Optional<WalletEntity> findByUserId(Long userId);
}
