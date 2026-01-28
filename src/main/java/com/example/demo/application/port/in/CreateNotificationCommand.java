package com.example.demo.application.port.in;

import com.example.demo.domain.vo.TransactionId;
import com.example.demo.domain.vo.UserId;
public record CreateNotificationCommand(
  TransactionId transactionId,
  UserId userId,
  String msg
) {

  public static CreateNotificationCommand of(
    final TransactionId transactionId, final Long userId, final String msg
  ) {
    return new CreateNotificationCommand(transactionId, UserId.of(userId), msg);
  }
}
