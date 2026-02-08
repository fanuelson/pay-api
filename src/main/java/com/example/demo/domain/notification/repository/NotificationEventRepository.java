package com.example.demo.domain.notification.repository;

import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.model.NotificationId;
import java.util.Optional;

public interface NotificationEventRepository {
  NotificationEvent save(NotificationEvent notification);
  Optional<NotificationEvent> findByNotificationId(NotificationId notificationId);
}
