package com.example.demo.infra.messaging.consumer;

import com.example.demo.application.handler.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
  topics = "${app.kafka.topics.transaction-events.name}",
  containerFactory = "transactionListenerContainerFactory"
)
public class TransactionEventConsumer {

  private final TranferEventHandler tranferEventHandler;

  @KafkaHandler
  public void handle(@Payload TransactionRequestedEvent event, Acknowledgment ack) {
    try {
      log.info("TransactionRequestedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
    }
  }

  @KafkaHandler
  public void handle(TransactionValidatedEvent event, Acknowledgment ack) {
    try {
      log.info("FANU TransactionValidatedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("FANU Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
      throw e;
    }
  }

  @RetryableTopic(
    backOff = @BackOff(delayString = "2000ms"),
//    exclude = {NotificationMaxAttemptsReachedException.class},
    attempts = "5",
    listenerContainerFactory = "transactionListenerContainerFactory",
    kafkaTemplate = "transactionKafkaTemplate"
  )
  @KafkaHandler
  public void handle(TransactionBalanceReservedEvent event, Acknowledgment ack) {
    try {
      log.info("TransactionBalanceReservedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
      throw e;
    }
  }

  @KafkaHandler
  public void handle(TransactionAuthorizedEvent event, Acknowledgment ack) {
    try {
      log.info("TransactionAuthorizedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
    }
  }

  @KafkaHandler
  public void handle(TransactionCreditedEvent event, Acknowledgment ack) {
    try {
      log.info("TransactionCreditedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
    }
  }

  @KafkaHandler
  public void handle(TransactionCompletedEvent event, Acknowledgment ack) {
    try {
      log.info("TransactionCompletedEvent [{}]", event);
      tranferEventHandler.handle(event);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.getTransactionId(), e);
    }
  }

}
