package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.handler.TransactionEvent;
import com.example.demo.application.port.out.event.TransactionEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionEventKafkaPublisher implements TransactionEventPublisher {

  @Autowired
  @Qualifier("transactionKafkaTemplate")
  private KafkaTemplate<String, TransactionEvent> kafkaTemplate;

  @Value("${app.kafka.topics.transaction-events.name}")
  private String topic;

  @Override
  public void publish(TransactionEvent event) {
    log.debug("Publishing TransactionEvent: transactionId={}", event.getTransactionId());
    kafkaTemplate.send(topic, event.getKey(), event);
  }

}
