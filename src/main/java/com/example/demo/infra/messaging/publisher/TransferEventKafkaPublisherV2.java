package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TransferEventKafkaPublisherV2 implements TransferEventPublisher {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${kafka.topics.transfer-events}")
  private String topic;

  @Override
  public void publish(TransferEvent event) {
    final var resolvedTopic = switch (event.status()) {
      case "PENDING" -> "transfer.validate";
      case "VALIDATED" -> "transfer.authorize";
      case "AUTHORIZED" -> "transfer.reserve";
      case "RESERVED" -> "transfer.credit";
      case "COMPLETED" -> "transfer.notify";
      case "FAILED" -> "transfer.failed";
      default -> topic;
    };
    publish(resolvedTopic, String.valueOf(event.payerId()), event);
  }

  private void publish(String topic, String key, Object event) {
    kafkaTemplate.send(topic, key, event);
  }
}
