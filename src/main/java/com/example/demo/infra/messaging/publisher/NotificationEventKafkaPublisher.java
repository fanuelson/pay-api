package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationEventKafkaPublisher implements NotificationEventPublisher {

  @Autowired
  @Qualifier("notificationKafkaTemplate")
  private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

  @Value("${app.kafka.topics.notification-events.name}")
  private String topic;

  @Override
  public void publish(NotificationEvent event) {
    var notificationId = event.notificationId();
    log.debug("Publishing NotificationEvent: notificationId={}", notificationId);
    kafkaTemplate.send(topic, notificationId.value(), event);
  }

}
