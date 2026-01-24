package com.example.demo.infra.http.input.resources;

public record SendNotificationRequest(
  String transactionId,
  Long recipientId,
  String recipientAddress,
  String channel,
  String message
) {
}
