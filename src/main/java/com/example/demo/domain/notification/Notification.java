package com.example.demo.domain.notification;

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
  private LocalDateTime createdAt;
  private LocalDateTime sentAt;

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
      .createdAt(LocalDateTime.now())
      .build();
  }

  public void sent() {
    this.status = NotificationStatus.SENT;
    this.sentAt = LocalDateTime.now();
  }

  public void failed() {
    this.status = NotificationStatus.FAILED;
  }

}
