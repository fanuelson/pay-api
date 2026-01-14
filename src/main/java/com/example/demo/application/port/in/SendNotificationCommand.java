package com.example.demo.application.port.in;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SendNotificationCommand {

  private final Long notificationId;

  public static SendNotificationCommand of(final Long notificationId) {
    return new SendNotificationCommand(notificationId);
  }
}
