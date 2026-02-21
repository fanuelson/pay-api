package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.listener.TransactionCreatedListener;
import com.example.demo.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCreatedEventConsumer {

  private final TransactionCreatedListener listener;

  @KafkaListener(
    topics = "transaction.created",
    containerFactory = "listenerContainerFactory"
  )
  @RetryableTopic(
    kafkaTemplate = "kafkaTemplate",
    backOff = @BackOff(delayString = "1000ms"),
    attempts = "10",
    numPartitions = "5",
    concurrency = "5",
    exclude = {BusinessException.class},
    dltStrategy = DltStrategy.FAIL_ON_ERROR
  )
  public void handle(@Payload TransactionCreatedEvent event, Acknowledgment ack) {
    log.info("[Consumer TransactionCreatedEventConsumer] {}", event);
    listener.handle(event);
    ack.acknowledge();
  }
}
