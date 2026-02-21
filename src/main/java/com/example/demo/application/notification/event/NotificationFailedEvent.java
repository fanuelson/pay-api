package com.example.demo.application.notification.event;

import com.example.demo.domain.notification.model.NotificationId;
import lombok.Getter;

@Getter
public class NotificationFailedEvent extends NotificationEvent {

  private final String cause;

  public NotificationFailedEvent(NotificationId notificationId, String cause) {
    super(notificationId);
    this.cause = cause;
  }

  public static NotificationFailedEvent from(NotificationId notificationId, String cause) {
    return new NotificationFailedEvent(notificationId, cause);
  }
}
