package com.example.demo.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

  private Long notificationId;
  private LocalDateTime timestamp;

  public static NotificationEvent of(final Long notificationId) {
    return new NotificationEvent(notificationId, LocalDateTime.now());
  }
}