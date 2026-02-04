package com.example.demo.domain.repository;

import com.example.demo.domain.model.Notification;
import com.example.demo.domain.vo.NotificationId;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
  Notification save(Notification notification);
  Notification update(Notification notification);
  void delete(NotificationId id);
  Optional<Notification> findById(NotificationId id);
  List<Notification> findPendingNotifications(int limit);
}
