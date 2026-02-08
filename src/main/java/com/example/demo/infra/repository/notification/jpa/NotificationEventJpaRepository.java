package com.example.demo.infra.repository.notification.jpa;

import com.example.demo.infra.repository.notification.jpa.entities.NotificationEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface NotificationEventJpaRepository extends JpaRepository<NotificationEventEntity, String> {

  Optional<NotificationEventEntity> findByNotificationId(Long notificationId);
}






