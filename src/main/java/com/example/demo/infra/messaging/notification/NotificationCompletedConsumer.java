package com.example.demo.infra.messaging.notification;

import com.example.demo.application.notification.event.NotificationCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCompletedConsumer {

  @KafkaListener(
    topics = "notification.completed",
    containerFactory = "listenerContainerFactory"
  )
  public void handle(
    NotificationCompletedEvent event,
    Acknowledgment ack) {
    log.info("[Consumer NotificationCompletedConsumer] {}", event);
    ack.acknowledge();
  }

}
