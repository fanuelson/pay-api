package com.example.demo.infra.messaging.publisher;

import com.example.demo.domain.model.OutboxEvent;
import com.example.demo.domain.model.OutboxEventStatus;
import com.example.demo.domain.notification.NotificationEvent;
import com.example.demo.domain.notification.NotificationEventPublisher;
import com.example.demo.domain.notification.NotificationFailedEvent;
import com.example.demo.domain.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class NotificationOutboxPublisher implements NotificationEventPublisher {

  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Value("${kafka.topics.notification-events}")
  private String topic;

  @Override
  public void publish(NotificationEvent event) {
    final var eventType = switch (event.status()) {
      case "PENDING" -> "NOTIFICATION_CREATED";
      case "SENT" -> "NOTIFICATION_SENT";
      case "FAILED" -> "NOTIFICATION_FAILED";
      default -> throw new IllegalStateException("Unexpected value: " + event.status());
    };

    try {
      var outboxEvent = OutboxEvent.builder()
        .aggregateId(event.notificationId().toString())
        .aggregateType("NOTIFICATION")
        .eventType(eventType)
        .topic(topic)
        .payload(objectMapper.writeValueAsString(event))
        .payloadType(NotificationEvent.class.getName())
        .status(OutboxEventStatus.PENDING)
        .attempts(0)
        .maxAttempts(5)
        .createdAt(LocalDateTime.now())
        .build();

      outboxEventRepository.save(outboxEvent);
      log.debug("Outbox event saved for notificationId={}", event.notificationId());
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize NotificationEvent", e);
    }
  }

  @Override
  public void publish(NotificationFailedEvent event) {

  }
}
