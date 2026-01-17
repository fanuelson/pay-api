package com.example.demo.application.port.out.event;

import com.example.demo.domain.model.Notification;
import java.time.LocalDateTime;
import java.util.function.Function;

public record NotificationEvent(Long notificationId, LocalDateTime timestamp) {

  public static NotificationEvent of(final Long notificationId) {
    return new NotificationEvent(notificationId, LocalDateTime.now());
  }

  public static Function<Notification, NotificationEvent> fromNotification() {
    return notification -> of(notification.getId());
  }

}