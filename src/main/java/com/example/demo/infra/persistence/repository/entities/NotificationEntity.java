package com.example.demo.infra.persistence.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
  name = "notifications",
  indexes = {
    @Index(name = "idx_notification_transaction", columnList = "transaction_id"),
    @Index(name = "idx_notification_recipient", columnList = "recipient_id"),
    @Index(name = "idx_notification_status_attempts", columnList = "status, attempts")
  }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "transaction_id", nullable = false, length = 36)
  private String transactionId;

  @Column(name = "recipient_id", nullable = false)
  private Long recipientId;

  @Column(name = "type", nullable = false, length = 20)
  private String type;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "attempts", nullable = false)
  private int attempts;

  @Column(name = "max_attempts", nullable = false)
  private int maxAttempts;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "sent_at")
  private LocalDateTime sentAt;
}