package com.example.demo.infra.messaging.notification.config;

import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.infra.messaging.KafkaTopicProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class NotificationTopicsProperties {

  private final NotificationRequestedTopicProperties notificationRequested;
  private final NotificationSentTopicProperties notificationSent;

  public KafkaTopicProperties resolve(NotificationEvent event) {
    return switch (event.getStatus()) {
      case REQUESTED -> notificationRequested;
      case SENT-> notificationSent;
      default -> throw new IllegalArgumentException("Event type not publishable: " + event.getClass().getSimpleName());
    };
  }

}
