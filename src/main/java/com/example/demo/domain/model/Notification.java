package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import java.time.LocalDateTime;
import java.util.function.Function;

@Data
@With
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

  public static Function<NotificationChannel, Notification> fromChannel(
    String transactionId, Long recipientId, String msg
  ) {
    return channel -> of(transactionId, recipientId, channel, msg);
  }

  public static NotificationBuilder builder() {
    return new NotificationBuilder();
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

  public static class NotificationBuilder {
    private Long id;
    private String transactionId;
    private Long recipientId;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String message;
    private int attempts;
    private int maxAttempts;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    NotificationBuilder() {
    }

    public NotificationBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public NotificationBuilder transactionId(String transactionId) {
      this.transactionId = transactionId;
      return this;
    }

    public NotificationBuilder recipientId(Long recipientId) {
      this.recipientId = recipientId;
      return this;
    }

    public NotificationBuilder channel(NotificationChannel channel) {
      this.channel = channel;
      return this;
    }

    public NotificationBuilder status(NotificationStatus status) {
      this.status = status;
      return this;
    }

    public NotificationBuilder message(String message) {
      this.message = message;
      return this;
    }

    public NotificationBuilder attempts(int attempts) {
      this.attempts = attempts;
      return this;
    }

    public NotificationBuilder maxAttempts(int maxAttempts) {
      this.maxAttempts = maxAttempts;
      return this;
    }

    public NotificationBuilder errorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }

    public NotificationBuilder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public NotificationBuilder sentAt(LocalDateTime sentAt) {
      this.sentAt = sentAt;
      return this;
    }

    public Notification build() {
      return new Notification(this.id, this.transactionId, this.recipientId, this.channel, this.status, this.message, this.attempts, this.maxAttempts, this.errorMessage, this.createdAt, this.sentAt);
    }

    public String toString() {
      return "Notification.NotificationBuilder(id=" + this.id + ", transactionId=" + this.transactionId + ", recipientId=" + this.recipientId + ", channel=" + this.channel + ", status=" + this.status + ", message=" + this.message + ", attempts=" + this.attempts + ", maxAttempts=" + this.maxAttempts + ", errorMessage=" + this.errorMessage + ", createdAt=" + this.createdAt + ", sentAt=" + this.sentAt + ")";
    }
  }
}
