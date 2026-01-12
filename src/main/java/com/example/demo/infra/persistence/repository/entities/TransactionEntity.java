package com.example.demo.infra.persistence.repository.entities;

import com.example.demo.domain.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_transaction_payer", columnList = "payer_id"),
                @Index(name = "idx_transaction_payee", columnList = "payee_id"),
                @Index(name = "idx_transaction_status", columnList = "status"),
                @Index(name = "idx_transaction_created_at", columnList = "created_at")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

  @Id
  @Column(name = "id", length = 36, nullable = false)
  private String id;

  @Column(name = "payer_id", nullable = false)
  private Long payerId;

  @Column(name = "payee_id", nullable = false)
  private Long payeeId;

  @Column(name = "amount_in_cents", nullable = false)
  private Long amountInCents;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private TransactionStatus status;

  @Column(name = "authorization_code", length = 255)
  private String authorizationCode;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @PrePersist
  @PreUpdate
  private void validate() {
    if (payerId.equals(payeeId)) {
      throw new IllegalArgumentException("Payer and payee cannot be the same");
    }
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}