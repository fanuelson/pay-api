package com.example.demo.infra.messaging.notification;

import com.example.demo.application.notification.event.NotificationCompletedEvent;
import com.example.demo.application.notification.event.NotificationCreatedEvent;
import com.example.demo.application.notification.event.NotificationFailedEvent;
import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.model.NotificationId;
import com.example.demo.domain.notification.repository.NotificationEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.retrytopic.RetryTopicHeaders;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
  topics = {"notification.created", "notification.completed", "notification.failed"},
  containerFactory = "listenerContainerFactory",
  groupId = "notification-event-group"
)
public class UpdateNotificationEvent {

  private final NotificationEventRepository notificationEventRepository;

  @KafkaHandler
  public void created(
    NotificationCreatedEvent event,
    @Header(name = RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS, required = false) Integer attempt,
    Acknowledgment ack
  ) {
    log.info("[Consumer NotificationEventsConsumer] {}, attempt: {}", event, attempt);
    final var notificationId = event.getNotificationId();
    final var notificationEvent = find(notificationId);
    notificationEventRepository.save(notificationEvent);
    ack.acknowledge();
  }

  @KafkaHandler
  public void completed(
    NotificationCompletedEvent event,
    Acknowledgment ack
  ) {
    log.info("[Consumer NotificationEventsConsumer] {}, attempt: {}", event);
    final var notificationId = event.getNotificationId();
    final var notificationEvent = find(notificationId);
    notificationEventRepository.save(notificationEvent.sent());
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(
    NotificationFailedEvent event,
    @Header(name = RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS, required = false) Integer attempt,
    Acknowledgment ack
  ) {
    log.info("[Consumer NotificationEventsConsumer] {}, attempt: {}", event, attempt);
    final var notificationId = event.getNotificationId();
    final var notificationEvent = find(notificationId);
    notificationEventRepository.save(notificationEvent.failed(event.getCause()));
    ack.acknowledge();
  }

  private NotificationEvent find(NotificationId notificationId) {
    final var notificationEvent = notificationEventRepository
      .findByNotificationId(notificationId)
      .orElse(NotificationEvent.requested(notificationId));

    return notificationEvent.attempt();
  }

}
