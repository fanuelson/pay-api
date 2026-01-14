package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.usecase.SendNotificationUseCase;
import com.example.demo.domain.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final SendNotificationUseCase sendNotificationUseCase;

  @KafkaListener(
    topics = "${kafka.topics.notification-events}",
    containerFactory = "notificationListenerContainerFactory"
  )
  public void onNotificationEvent(@Payload NotificationEvent event, Acknowledgment ack) {
    sendNotificationUseCase.execute(SendNotificationCommand.of(event.getNotificationId()));
    ack.acknowledge();
  }

}
