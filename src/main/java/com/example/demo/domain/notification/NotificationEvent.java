package com.example.demo.domain.notification;

public record NotificationEvent(
  Long notificationId,
  String status
) {

  public static NotificationEvent of(final Long notificationId, final NotificationStatus status) {
    return new NotificationEvent(notificationId, status.name());
  }

  public static NotificationEvent of(final Long notificationId) {
    return of(notificationId, NotificationStatus.PENDING);
  }

}