package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final SendNotificationUseCase sendNotificationUseCase;

  @RetryableTopic(
    backOff = @BackOff(delayString = "#{@notificationTopicProperties.delay}ms"),
    attempts = "#{@notificationTopicProperties.maxAttempts}",
    numPartitions = "#{@notificationTopicProperties.partitions}",
    listenerContainerFactory = "notificationListenerContainerFactory",
    kafkaTemplate = "notificationKafkaTemplate"
  )
  @KafkaListener(
    topics = "#{@notificationTopicProperties.name}",
    containerFactory = "notificationListenerContainerFactory"
  )
  public void handle(@Payload NotificationEvent event, Acknowledgment ack) {
    final var notificationId = event.notificationId();
    log.info("Received NotificationEvent: [notificationId={}]", notificationId);
    final var command = SendNotificationCommand.of(notificationId);
    sendNotificationUseCase.execute(command);
    ack.acknowledge();
  }

  @KafkaListener(
    topics = "#{@notificationTopicProperties.retryName}",
    containerFactory = "notificationListenerContainerFactory",
    groupId = "notification-dlt-group"
  )
  public void handleFailure(@Payload NotificationEvent event, Acknowledgment ack) {
    final var notificationId = event.notificationId();
    log.info("Received Failed NotificationEvent: [notificationId={}]", notificationId);
    ack.acknowledge();
  }

}
