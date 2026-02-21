package com.example.demo.domain.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import java.time.LocalDateTime;

@With
@Getter
@RequiredArgsConstructor
public class NotificationEvent {

  private final NotificationEventId id;
  private final NotificationId notificationId;
  private final NotificationStatus status;
  private final String errorMessage;
  private final long attempts;
  private final LocalDateTime createdAt;

  public static NotificationEvent requested(NotificationId notificationId) {
    return new NotificationEvent(
      NotificationEventId.generate(),
      notificationId,
      NotificationStatus.REQUESTED,
      null,
      0,
      LocalDateTime.now()
    );
  }

  public NotificationEvent attempt() {
    return new NotificationEvent(
      id,
      notificationId,
      status,
      errorMessage,
      attempts + 1,
      createdAt
    );
  }

  public NotificationEvent failed(String cause) {
    return new NotificationEvent(
      id,
      notificationId,
      NotificationStatus.FAILED,
      cause,
      attempts,
      createdAt
    );
  }

  public NotificationEvent sent() {
    return new NotificationEvent(
      id,
      notificationId,
      NotificationStatus.SENT,
      null,
      attempts,
      createdAt
    );
  }
}
