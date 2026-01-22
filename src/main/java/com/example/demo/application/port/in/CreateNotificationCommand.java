package com.example.demo.application.port.in;

import com.example.demo.domain.vo.TransactionId;
public record CreateNotificationCommand(
  TransactionId transactionId,
  Long userId,
  String msg
) {

  public static CreateNotificationCommand of(
    final TransactionId transactionId, final Long userId, final String msg
  ) {
    return new CreateNotificationCommand(transactionId, userId, msg);
  }
}
