package com.example.demo.application.notification.event;

import com.example.demo.domain.notification.model.NotificationId;

public class NotificationCreatedEvent extends NotificationEvent {

  public NotificationCreatedEvent(NotificationId notificationId) {
    super(notificationId);
  }

  public static NotificationCreatedEvent from(NotificationId notificationId) {
    return new NotificationCreatedEvent(notificationId);
  }
}
