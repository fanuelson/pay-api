package com.example.demo.infra.repository.jpa;

import com.example.demo.infra.repository.jpa.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {

  Optional<WalletEntity> findByUserId(Long userId);
  boolean existsByUserId(Long userId);

  @Modifying
  @Query("UPDATE WalletEntity w SET w.balanceInCents = w.balanceInCents + :amount, w.version = w.version + 1 WHERE w.id = :id")
  int creditBalance(@Param("id") Long id, @Param("amount") Long amount);

  @Modifying
  @Query("UPDATE WalletEntity w SET w.balanceInCents = w.balanceInCents - :amount, w.version = w.version + 1 WHERE w.id = :id AND w.balanceInCents >= :amount")
  int debitBalance(@Param("id") Long id, @Param("amount") Long amount);
}