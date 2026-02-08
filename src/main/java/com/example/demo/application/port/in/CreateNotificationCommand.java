package com.example.demo.application.port.in;

import com.example.demo.domain.user.model.UserId;

public record CreateNotificationCommand(
  UserId userId,
  String msg
) {

  public static CreateNotificationCommand of(final Long userId, final String msg
  ) {
    return new CreateNotificationCommand(UserId.of(userId), msg);
  }
}
