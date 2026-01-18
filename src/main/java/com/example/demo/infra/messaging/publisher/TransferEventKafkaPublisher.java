package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.application.port.out.event.TransferEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferEventKafkaPublisher implements TransferEventPublisher {

  @Autowired
  @Qualifier("transferKafkaTemplate")
  private KafkaTemplate<String, TransferEvent> kafkaTemplate;

  @Value("${app.kafka.topics.transfer-events.name}")
  private String topic;

  @Override
  public void publish(TransferEvent event) {
    log.info("Publishing TransferEvent: transactionId={}, status={}", event.transactionId(), event.status());

    kafkaTemplate.send(topic, String.valueOf(event.payerId()), event)
      .whenComplete((result, ex) -> {
        if (ex != null) {
          log.error("Failed to publish: transactionId={}", event.transactionId(), ex);
        } else {
          log.debug("Published: transactionId={}, partition={}, offset={}",
            event.transactionId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        }
      });
  }
}
