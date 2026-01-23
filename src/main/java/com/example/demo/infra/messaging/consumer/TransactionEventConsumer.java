package com.example.demo.infra.messaging.consumer;

import com.example.demo.application.handler.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RetryableTopic(
  backOff = @BackOff(delay = 2_000),
  attempts = "5",
  listenerContainerFactory = "transactionListenerContainerFactory",
  kafkaTemplate = "transactionKafkaTemplate", dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(
  topics = "${app.kafka.topics.transaction-events.name}",
  containerFactory = "transactionListenerContainerFactory"
)
public class TransactionEventConsumer {

  private final TransactionEventHandler transactionEventHandler;

  @KafkaHandler
  public void handle(@Payload TransactionRequestedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionValidatedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionBalanceReservedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionAuthorizedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionCreditedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionCompletedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(@Payload TransactionFailedEvent event, Acknowledgment ack) {
    transactionEventHandler.handle(event);
    ack.acknowledge();
  }

  @DltHandler
  public void dlt(
    @Payload TransactionEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    if (event instanceof TransactionBalanceReservedEvent) {
      final var authorizationFailedEvent = TransactionAuthorizationFailedEvent.from(event).withMsg(errorMessage);
      transactionEventHandler.handle(authorizationFailedEvent);
    } else {
      final var failedEvent = TransactionFailedEvent.from(event, errorMessage);
      transactionEventHandler.handle(failedEvent);
    }
    ack.acknowledge();
  }

  @KafkaHandler(isDefault = true)
  public void onDefault(Object unknown) {
    log.warn("Event without handler: [{}]", unknown);
  }

}
