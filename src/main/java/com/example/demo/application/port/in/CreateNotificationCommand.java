package com.example.demo.application.port.in;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateNotificationCommand {

  private final String transactionId;
  private final Long userId;
  private final String msg;

  public static CreateNotificationCommand of(
    final String transactionId, final Long userId, final String msg
  ) {
    return new CreateNotificationCommand(transactionId, userId, msg);
  }
}
