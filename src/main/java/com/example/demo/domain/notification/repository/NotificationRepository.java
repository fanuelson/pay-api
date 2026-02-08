package com.example.demo.domain.notification.repository;

import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.model.NotificationId;
import java.util.Optional;

public interface NotificationRepository {
  Notification save(Notification notification);
  Optional<Notification> findById(NotificationId id);
}
