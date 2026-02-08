package com.example.demo.application.port.in;

import com.example.demo.domain.notification.model.NotificationId;

public record SendNotificationCommand(NotificationId notificationId) {

  public static SendNotificationCommand of(final NotificationId notificationId) {
    return new SendNotificationCommand(notificationId);
  }
}
