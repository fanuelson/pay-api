package com.example.demo.infra.messaging.publisher;

import com.example.demo.domain.model.NotificationEvent;
import com.example.demo.domain.port.event.NotificationEventPublisher;
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

  @Value("${kafka.topics.notification-events}")
  private String topic;

  @Override
  public void publish(NotificationEvent event) {
    var notificationId = event.getNotificationId();
    log.info("Publishing NotificationEvent: notificationId={}", notificationId);

    kafkaTemplate.send(topic, notificationId.toString(), event)
      .whenComplete((result, ex) -> {
        if (ex != null) {
          log.error("Failed to publish: notificationId={}", notificationId, ex);
        } else {
          log.debug("Published: notificationId={}, partition={}, offset={}",
            notificationId, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        }
      });
  }
}
