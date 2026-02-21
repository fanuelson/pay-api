package com.example.demo.application.notification.event;

import com.example.demo.domain.notification.model.NotificationId;
import lombok.Getter;

@Getter
public abstract class NotificationEvent {

  private final NotificationId notificationId;

  protected NotificationEvent(NotificationId notificationId) {
    this.notificationId = notificationId;
  }
}
