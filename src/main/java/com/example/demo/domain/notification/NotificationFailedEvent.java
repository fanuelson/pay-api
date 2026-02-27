package com.example.demo.domain.notification;

public record NotificationFailedEvent(
  Long notificationId,
  String reason
) {

  public static NotificationFailedEvent of(final Long notificationId, final String reason) {
    return new NotificationFailedEvent(notificationId, reason);
  }

}