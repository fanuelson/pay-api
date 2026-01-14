package com.example.demo.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  private Long id;
  private String transactionId;
  private Long recipientId;
  private NotificationChannel channel;          // EMAIL, SMS
  private NotificationStatus status;        // PENDING, SENT, FAILED
  private String message;
  private int attempts;
  private int maxAttempts;
  private String errorMessage;
  private LocalDateTime createdAt;
  private LocalDateTime sentAt;

  public Notification(String transactionId, Long recipientId, NotificationChannel channel, NotificationStatus status) {
    this.id = null;
    this.transactionId = transactionId;
    this.recipientId = recipientId;
    this.channel = channel;
    this.status = status;
    this.attempts = 0;
    this.maxAttempts = 3;
    this.errorMessage = null;
    this.createdAt = LocalDateTime.now();
    this.sentAt = null;
  }

  public static Notification of(
    String transactionId,
    Long recipientId,
    NotificationChannel channel,
    String msg
  ) {
    return Notification.builder()
      .transactionId(transactionId)
      .recipientId(recipientId)
      .channel(channel)
      .status(NotificationStatus.PENDING)
      .message(msg)
      .attempts(0)
      .maxAttempts(3)
      .createdAt(LocalDateTime.now())
      .build();
  }

  public boolean is(NotificationStatus status) {
    return status.equals(this.status);
  }

  public boolean isNot(NotificationStatus status) {
    return !status.equals(this.status);
  }

  public void incrementAttempts() {
    this.attempts++;
  }

  public boolean hasReachedMaxAttempts() {
    return this.attempts >= this.maxAttempts;
  }

  public void sent() {
    this.status = NotificationStatus.SENT;
    this.sentAt = LocalDateTime.now();
  }

  public void failed(String reason) {
    this.status = NotificationStatus.FAILED;
    this.errorMessage = reason;
    incrementAttempts();
  }

  public boolean canRetry() {
    return !hasReachedMaxAttempts() && !NotificationStatus.SENT.equals(this.status);
  }
}
