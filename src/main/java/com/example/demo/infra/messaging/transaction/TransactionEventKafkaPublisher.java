package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.port.out.event.TransactionEventPublisher;
import com.example.demo.domain.event.TransactionEvent;
import com.example.demo.domain.event.TransactionEventRecord;
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
    kafkaTemplate.send(topic, event.getKey(), event);
  }

  @Override
  public void publish(TransactionEventRecord event) {
    kafkaTemplate.send(topic, event.key().key(), event.event());
  }

}
