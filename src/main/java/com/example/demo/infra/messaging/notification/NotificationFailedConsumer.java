package com.example.demo.infra.messaging.notification;

import com.example.demo.application.notification.event.NotificationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationFailedConsumer {

  @KafkaListener(
    topics = "notification.failed",
    containerFactory = "listenerContainerFactory"
  )
  public void handle(
    NotificationFailedEvent event,
    Acknowledgment ack) {
    log.info("[Consumer NotificationFailedConsumer] {}", event);
    ack.acknowledge();
  }

}
