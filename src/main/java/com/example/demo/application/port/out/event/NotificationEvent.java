package com.example.demo.application.port.out.event;

import com.example.demo.domain.model.Notification;
import java.time.LocalDateTime;
import java.util.function.Function;

public record NotificationEvent(
  Long notificationId,
  Notification notification,
  LocalDateTime timestamp
) {

  public static NotificationEvent of(final Long notificationId) {
    return new NotificationEvent(notificationId, null, LocalDateTime.now());
  }

  public static NotificationEvent of(final Notification notification) {
    return new NotificationEvent(notification.getId(), notification, LocalDateTime.now());
  }

  public static Function<Notification, NotificationEvent> fromNotification() {
    return NotificationEvent::of;
  }

}