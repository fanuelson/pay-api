package com.example.demo.infra.messaging.notification;

import com.example.demo.application.notification.*;
import com.example.demo.application.notification.event.NotificationCompletedEvent;
import com.example.demo.application.notification.event.NotificationCreatedEvent;
import com.example.demo.application.notification.event.NotificationEvent;
import com.example.demo.application.notification.event.NotificationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventKafkaPublisher implements NotificationEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publish(NotificationEvent event) {
    final var topic = switch (event) {
      case NotificationCreatedEvent _e -> "notification.created";
      case NotificationCompletedEvent _e -> "notification.completed";
      case NotificationFailedEvent _e -> "notification.failed";
      default -> throw new IllegalStateException("Unexpected value: " + event);
    };

    final var notificationId = event.getNotificationId();
    kafkaTemplate.send(topic, notificationId.value(), event);
  }
}
