package com.example.demo.domain.model;

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
  NotificationStatus type;

  @Builder.Default
  LocalDateTime timestamp = LocalDateTime.now();
}