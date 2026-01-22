package com.example.demo.infra.messaging.consumer;

import com.example.demo.application.handler.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RetryableTopic(
  backOff = @BackOff(delay = 2_000),
  attempts = "3",
  listenerContainerFactory = "transactionListenerContainerFactory",
  kafkaTemplate = "transactionKafkaTemplate"
)
@KafkaListener(
  topics = "${app.kafka.topics.transaction-events.name}",
  containerFactory = "transactionListenerContainerFactory"
)
public class TransactionEventConsumer {

  private final TransactionEventHandler transactionEventHandler;

  @KafkaHandler
  public void handle(@Payload TransactionRequestedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionValidatedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionBalanceReservedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionAuthorizedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionCreditedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionCompletedEvent event) {
    transactionEventHandler.handle(event);
  }

  @KafkaHandler
  public void handle(@Payload TransactionFailedEvent event) {
    transactionEventHandler.handle(event);
  }

  @DltHandler
  public void dlt(
    @Payload TransactionBalanceReservedEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage
  ) {
    final var authorizationFailedEvent = TransactionAuthorizationFailedEvent.from(event).withMsg(errorMessage);
    transactionEventHandler.handle(authorizationFailedEvent);
  }

  @KafkaHandler(isDefault = true)
  public void onDefault(Object unknown) {
    log.warn("Event without handler: [{}]", unknown);
  }

  @DltHandler
  public void dlt(Object event) {
    log.error("DLT event with no treatment: {}", event);
  }

}
