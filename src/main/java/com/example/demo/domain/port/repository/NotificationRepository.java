package com.example.demo.domain.port.repository;

import com.example.demo.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
  Notification save(Notification notification);
  Notification update(Long id, Notification notification);
  void delete(Long id);
  Optional<Notification> findById(Long id);
  List<Notification> findByTransactionId(String transactionId);
  List<Notification> findPendingNotifications(int limit);
}
