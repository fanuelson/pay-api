package com.example.demo.infra.persistence.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
  name = "wallets",
  indexes = {
    @Index(name = "idx_wallet_user_id", columnList = "user_id")
  }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(name = "balance_in_cents", nullable = false)
  private Long balanceInCents;

  @Version
  @Column(name = "version", nullable = false)
  private int version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  private void onPrePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    if (updatedAt == null) {
      updatedAt = LocalDateTime.now();
    }
    validate();
  }

  @PreUpdate
  private void onPreUpdate() {
    updatedAt = LocalDateTime.now();
    validate();
  }

  private void validate() {
    if (balanceInCents < 0) {
      throw new IllegalArgumentException("Balance cannot be negative");
    }
  }
}
