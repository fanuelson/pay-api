package com.example.demo.domain.model;

import com.example.demo.domain.helper.DateTimeHelper;
import lombok.*;
import java.time.LocalDateTime;
import java.util.function.Function;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  private Long id;
  private String transactionId;
  private Long recipientId;
  private String recipientAddress;
  private NotificationChannel channel;          // EMAIL, SMS
  private NotificationStatus status;        // PENDING, SENT, FAILED
  private String message;
  private RetryOptions retry;
  private String errorMessage;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime sentAt;

  public static Notification pending(
    String transactionId,
    Long recipientId,
    String recipientAddress,
    NotificationChannel channel,
    String msg
  ) {
    return Notification.builder()
      .transactionId(transactionId)
      .recipientId(recipientId)
      .recipientAddress(recipientAddress)
      .channel(channel)
      .status(NotificationStatus.PENDING)
      .message(msg)
      .retry(new RetryOptions(5))
      .createdAt(DateTimeHelper.now())
      .updatedAt(DateTimeHelper.now())
      .build();
  }

  public static Function<NotificationChannel, Notification> fromChannel(
    String transactionId, Long recipientId, String recipientAddress, String msg
  ) {
    return channel -> pending(transactionId, recipientId, recipientAddress, channel, msg);
  }

  public boolean isNot(NotificationStatus status) {
    return !status.equals(this.status);
  }

  public void sent() {
    this.status = NotificationStatus.SENT;
    this.sentAt = DateTimeHelper.now();
  }

  public void failed(String reason) {
    this.status = NotificationStatus.FAILED;
    this.errorMessage = reason;
    this.retry = this.retry.incrementAttempts();
  }

  public boolean canRetry() {
    return this.retry.canRetry() && isNot(NotificationStatus.SENT);
  }

}
