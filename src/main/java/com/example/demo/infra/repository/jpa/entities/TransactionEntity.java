package com.example.demo.infra.repository.jpa.entities;

import com.example.demo.domain.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
  @Column(name = "id", nullable = false)
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

  @Column(name = "authorization_code")
  private String authorizationCode;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @PrePersist
  private void prePersist() {
    createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

}