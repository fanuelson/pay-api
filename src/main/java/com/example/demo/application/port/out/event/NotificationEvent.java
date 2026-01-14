package com.example.demo.application.port.out.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

  private Long notificationId;
  private LocalDateTime timestamp;

  public static NotificationEvent of(final Long notificationId) {
    return new NotificationEvent(notificationId, LocalDateTime.now());
  }

}