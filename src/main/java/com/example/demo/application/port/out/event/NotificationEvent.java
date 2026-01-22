package com.example.demo.application.port.out.event;

import com.example.demo.domain.helper.DateTimeHelper;
import com.example.demo.domain.vo.NotificationId;
import java.time.LocalDateTime;

public record NotificationEvent(
  NotificationId notificationId,
  LocalDateTime timestamp
) {

  public static NotificationEvent request(final NotificationId notificationId) {
    return new NotificationEvent(notificationId, DateTimeHelper.now());
  }

}