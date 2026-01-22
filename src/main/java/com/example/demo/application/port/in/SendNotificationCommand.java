package com.example.demo.application.port.in;

import com.example.demo.domain.vo.NotificationId;

public record SendNotificationCommand(NotificationId notificationId) {

  public static SendNotificationCommand of(final NotificationId notificationId) {
    return new SendNotificationCommand(notificationId);
  }
}
