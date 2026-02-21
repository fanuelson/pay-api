package com.example.demo.application.notification.event;

import com.example.demo.domain.notification.model.NotificationId;

public class NotificationCompletedEvent extends NotificationEvent {

  public NotificationCompletedEvent(NotificationId notificationId) {
    super(notificationId);
  }

  public static NotificationCompletedEvent from(NotificationId notificationId) {
    return new NotificationCompletedEvent(notificationId);
  }
}
