package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.transaction.TransactionCompletedNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExecuteCreateTransactionCompletedNotification {

  private final TransactionCompletedNotification createTransactionCompletedNotification;

  @KafkaListener(
    topics = "transaction.completed",
    containerFactory = "listenerContainerFactory",
    groupId = "transaction-notification-group"
  )
  public void handle(TransactionCompletedEvent event, Acknowledgment ack) {
    log.info("[Consumer ExecuteCreateTransactionCompletedNotification] {}", event);
//    createTransactionCompletedNotification.execute(event);
    ack.acknowledge();
  }
}
