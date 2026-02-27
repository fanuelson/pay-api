package com.example.demo.domain.notification;

public interface NotificationEventPublisher {
  void publish(NotificationEvent event);
  void publish(NotificationFailedEvent event);
}
