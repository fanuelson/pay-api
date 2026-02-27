package com.example.demo.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NotificationV2 {

  private Long id;
  private String recipientAddress;
  private NotificationChannel channel;
  private String message;
  private LocalDateTime createdAt;

  public NotificationV2(String recipientAddress, NotificationChannel channel, String message) {
    this.id = null;
    this.recipientAddress = recipientAddress;
    this.channel = channel;
    this.message = message;
    this.createdAt = LocalDateTime.now();
  }

}
