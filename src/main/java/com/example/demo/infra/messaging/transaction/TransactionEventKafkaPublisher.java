package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.transaction.TransactionEvent;
import com.example.demo.application.transaction.TransactionEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventKafkaPublisher implements TransactionEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publish(TransactionEvent event) {
    final var topic = switch (event) {
      case TransactionCreatedEvent _e -> "transaction.created";
      case TransactionCompletedEvent _e -> "transaction.completed";
      case TransactionFailedEvent _e -> "transaction.failed";
      default -> throw new IllegalStateException("Unexpected value: " + event);
    };

    final var transactionId = event.getTransactionId();
    final var payerId = event.getTransactionId();
    kafkaTemplate.send(topic, payerId.value(), event);
  }
}
