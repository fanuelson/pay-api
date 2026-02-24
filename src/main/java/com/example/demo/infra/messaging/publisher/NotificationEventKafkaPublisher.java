package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class NotificationEventKafkaPublisher implements NotificationEventPublisher {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${kafka.topics.notification-events}")
  private String topic;

  @Override
  public void publish(NotificationEvent event) {
    var notificationId = event.getNotificationId();
    log.info("Publishing NotificationEvent: notificationId={}", notificationId);

    kafkaTemplate.send(topic, notificationId.toString(), event)
      .whenComplete((result, ex) -> handleComplete(notificationId, result, ex));
  }

  private void handleComplete(Long notificationId, SendResult<String, Object> result, Throwable ex) {
    if (nonNull(ex)) {
      log.error("Failed to publish: notificationId={}", notificationId, ex);
    } else {
      log.debug("Published: notificationId={}, partition={}, offset={}",
        notificationId, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
    }
  }
}
