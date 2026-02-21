package com.example.demo.infra.messaging.notification;

import com.example.demo.application.notification.event.NotificationCreatedEvent;
import com.example.demo.application.notification.NotificationEventPublisher;
import com.example.demo.application.notification.event.NotificationFailedEvent;
import com.example.demo.application.notification.SendNotificationUseCase;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.retrytopic.RetryTopicHeaders;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RetryableTopic(
  backOff = @BackOff(delayString = "2000ms"),
  attempts = "2",
  numPartitions = "5",
  concurrency = "5"
)
@KafkaListener(
  topics = "notification.created",
  containerFactory = "listenerContainerFactory"
)
public class SendNotification {

  private final SendNotificationUseCase sendNotificationUseCase;
  private final NotificationEventPublisher publisher;
  private final MeterRegistry meterRegistry;

  @KafkaHandler(isDefault = true)
  public void handle(
    NotificationCreatedEvent event,
    @Header(name = RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS, required = false) Integer attempt,
    Acknowledgment ack
  ) {
    log.info("[Consumer NotificationCreatedConsumer] {}, attempt: {}", event, attempt);
    sendNotificationUseCase.handle(event);
    ack.acknowledge();
  }

  @DltHandler
  public void handleDlt(
    NotificationCreatedEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    log.warn("[DLT Consumer NotificationCreatedConsumer] {}, {}", event, errorMessage);
    publisher.publish(NotificationFailedEvent.from(event.getNotificationId(), errorMessage));
    meterRegistry.counter("notification.failed").increment();
    ack.acknowledge();
  }

}
