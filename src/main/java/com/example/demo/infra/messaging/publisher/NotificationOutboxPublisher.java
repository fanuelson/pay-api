package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.model.OutboxEvent;
import com.example.demo.domain.model.OutboxEventStatus;
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
    try {
      var outboxEvent = OutboxEvent.builder()
        .aggregateId(event.getNotificationId().toString())
        .aggregateType("NOTIFICATION")
        .eventType("NOTIFICATION_CREATED")
        .topic(topic)
        .payload(objectMapper.writeValueAsString(event))
        .payloadType(NotificationEvent.class.getName())
        .status(OutboxEventStatus.PENDING)
        .attempts(0)
        .maxAttempts(5)
        .createdAt(LocalDateTime.now())
        .build();

      outboxEventRepository.save(outboxEvent);
      log.debug("Outbox event saved for notificationId={}", event.getNotificationId());
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize NotificationEvent", e);
    }
  }
}
