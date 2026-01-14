package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  private Long id;
  private String transactionId;
  private Long recipientId;
  private NotificationType type;          // EMAIL, SMS
  private NotificationStatus status;        // PENDING, SENT, FAILED
  private int attempts;
  private int maxAttempts;
  private String errorMessage;
  private LocalDateTime createdAt;
  private LocalDateTime sentAt;

  public Notification(String transactionId, Long recipientId, NotificationType type, NotificationStatus status) {
    this.id = null;
    this.transactionId = transactionId;
    this.recipientId = recipientId;
    this.type = type;
    this.status = status;
    this.attempts = 0;
    this.maxAttempts = 3;
    this.errorMessage = null;
    this.createdAt = LocalDateTime.now();
    this.sentAt = null;
  }

  public void incrementAttempts() {
    this.attempts++;
  }

  public boolean hasReachedMaxAttempts() {
    return this.attempts >= this.maxAttempts;
  }

  public void markAsSent() {
    this.status = NotificationStatus.SENT;
    this.sentAt = LocalDateTime.now();
  }

  public void markAsFailed(String reason) {
    this.status = NotificationStatus.FAILED;
    this.errorMessage = reason;
    incrementAttempts();
  }

  public boolean canRetry() {
    return !hasReachedMaxAttempts() && !NotificationStatus.SENT.equals(this.status);
  }
}
