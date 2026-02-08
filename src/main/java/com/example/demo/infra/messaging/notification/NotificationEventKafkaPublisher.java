package com.example.demo.infra.messaging.notification;

import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.infra.messaging.notification.config.NotificationTopicsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventKafkaPublisher implements NotificationEventPublisher {

  private final KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate;
  private final NotificationTopicsProperties topicsProperties;

  @Override
  public void publish(NotificationEvent event) {
    final var notificationId = event.getNotificationId();
    final var topic = topicsProperties.resolve(event).getName();
    notificationKafkaTemplate.send(topic, notificationId.value(), event);
  }

}
