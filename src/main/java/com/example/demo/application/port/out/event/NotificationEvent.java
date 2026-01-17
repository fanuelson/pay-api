package com.example.demo.application.port.out.event;

import java.time.LocalDateTime;

public record NotificationEvent(Long notificationId, LocalDateTime timestamp) {

  public static NotificationEvent of(final Long notificationId) {
    return new NotificationEvent(notificationId, LocalDateTime.now());
  }

}