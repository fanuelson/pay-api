package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.handler.TransactionEventHandler;
import com.example.demo.domain.event.*;
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
  backOff = @BackOff(delayString = "#{@transactionTopicProperties.delay}ms"),
  attempts = "#{@transactionTopicProperties.maxAttempts}",
  listenerContainerFactory = "transactionListenerContainerFactory",
  kafkaTemplate = "transactionKafkaTemplate",
  dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(
  topics = "#{@transactionTopicProperties.name}",
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
  public void handle(@Payload TransactionAuthorizationRequestedEvent event, Acknowledgment ack) {
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


  @KafkaHandler(isDefault = true)
  public void onDefault(Object unknown) {
    log.warn("Event without handler: [{}]", unknown);
  }

  @DltHandler
  public void dlt(
    @Payload TransactionEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    log.warn("FANU AQUI TAMBÉM [{}]", event);
    if (event instanceof TransactionAuthorizationRequestedEvent) {
      log.warn("FANU NOTHING");
    } else {
      final var failedEvent = TransactionFailedEvent.from(event, errorMessage);
      transactionEventHandler.handle(failedEvent);
    }
    ack.acknowledge();
  }


}
