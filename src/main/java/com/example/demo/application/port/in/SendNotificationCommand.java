package com.example.demo.application.port.in;


public record SendNotificationCommand(Long notificationId) {

  public static SendNotificationCommand of(final Long notificationId) {
    return new SendNotificationCommand(notificationId);
  }
}
