package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RetryableTopic(
  backOff = @BackOff(delayString = "5000ms"),
  attempts = "5",
  numPartitions = "5",
  concurrency = "5"
)
@KafkaListener(
  topics = "${kafka.topics.notification-events}",
  containerFactory = "listenerContainerFactory",
  groupId = "notification-group"
)
public class NotificationEventListener {

  private final SendNotificationUseCase sendNotificationUseCase;

  @KafkaHandler(isDefault = true)
  public void onNotificationEvent(NotificationEvent event, Acknowledgment ack) {
    sendNotificationUseCase.execute(SendNotificationCommand.of(event.getNotificationId()));
    ack.acknowledge();
  }

  @DltHandler
  public void handleDlt(
    NotificationEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    sendNotificationUseCase.handleFailure(event.getNotificationId(), errorMessage);
    ack.acknowledge();
  }

}
