package com.example.demo.infra.messaging.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionFailedEventConsumer {

  @KafkaListener(
    topics = "transaction.failed",
    containerFactory = "listenerContainerFactory"
  )
  public void handle(@Payload TransactionFailedEvent event, Acknowledgment ack) {
    log.info("[Consumer TransactionFailedEventConsumer], {}", event);
    ack.acknowledge();
  }
}
