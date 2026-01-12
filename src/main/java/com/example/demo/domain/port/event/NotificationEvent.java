package com.example.demo.domain.port.event;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class NotificationEvent {

  String transactionId;
  Long recipientId;
  String recipientEmail;
  String message;
  NotificationType type;

  @Builder.Default
  LocalDateTime timestamp = LocalDateTime.now();
}