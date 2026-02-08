package com.example.demo.infra.messaging.notification;

import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.usecase.SendNotificationUseCase;
import com.example.demo.domain.notification.model.NotificationEvent;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor

public class NotificationRequestedConsumer {

  private final SendNotificationUseCase sendNotificationUseCase;
  private final MeterRegistry meterRegistry;

  @RetryableTopic(
    backOff = @BackOff(delayString = "#{@notificationTopicsProperties.notificationRequested.delay}ms"),
    attempts = "#{@notificationTopicsProperties.notificationRequested.maxAttempts}",
    numPartitions = "#{@notificationTopicsProperties.notificationRequested.partitions}",
    listenerContainerFactory = "notificationListenerContainerFactory",
    kafkaTemplate = "notificationKafkaTemplate"
  )
  @KafkaListener(
    topics = "#{@notificationTopicsProperties.notificationRequested.name}",
    containerFactory = "notificationListenerContainerFactory"
  )
  public void handle(
    @Payload NotificationEvent event,
    Acknowledgment ack
  ) {
    final var notificationId = event.getNotificationId();
    final var command = SendNotificationCommand.of(notificationId);
    sendNotificationUseCase.execute(command);
    ack.acknowledge();
  }

  @KafkaListener(
    topics = "#{@notificationTopicsProperties.notificationRequested.retryName}",
    containerFactory = "notificationListenerContainerFactory",
    groupId = "notification-retry-group"
  )
  public void handleRetry(
    @Payload NotificationEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    log.warn("Retry handler {}, {}", event, errorMessage);
    sendNotificationUseCase.onRetry(event);
    ack.acknowledge();
  }


  @KafkaListener(
    topics = "#{@notificationTopicsProperties.notificationRequested.dltName}",
    containerFactory = "notificationListenerContainerFactory",
    groupId = "notification-dlt-group"
  )
  public void handleDlt(
    @Payload NotificationEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    sendNotificationUseCase.onFail(event, errorMessage);
    meterRegistry.counter("notification.failed").increment();
    ack.acknowledge();
  }

}
