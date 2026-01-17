package com.example.demo.application.port.in;

public record CreateNotificationCommand(
  String transactionId,
  Long userId,
  String msg
) {

  public static CreateNotificationCommand of(
    final String transactionId, final Long userId, final String msg
  ) {
    return new CreateNotificationCommand(transactionId, userId, msg);
  }
}
