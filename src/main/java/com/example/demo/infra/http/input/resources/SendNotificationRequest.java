package com.example.demo.infra.http.input.resources;

public record SendNotificationRequest(
  Long recipientId,
  String recipientAddress,
  String channel,
  String message
) {
}
