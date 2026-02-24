package com.example.demo.infra.messaging.listener;

import com.example.demo.application.chain.TransferChain;
import com.example.demo.application.chain.transfer.TransferContext;
import com.example.demo.application.chain.transfer.step.CreditStep;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.domain.exception.InsufficientBalanceException;
import com.example.demo.domain.model.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RetryableTopic(
  backOff = @BackOff(delayString = "1000ms"),
  attempts = "5",
  numPartitions = "5",
  concurrency = "5",
  dltStrategy = DltStrategy.FAIL_ON_ERROR,
  exclude = {InsufficientBalanceException.class, IllegalArgumentException.class}
)
@KafkaListener(
  topics = "transfer.credit",
  containerFactory = "listenerContainerFactory",
  groupId = "credit-group"
)
public class CreditEventListener {

  private final CreditStep creditStep;
  private final TransferChain chain;
  private final TransferEventPublisher publisher;

  @KafkaHandler(isDefault = true)
  public void handle(TransferEvent event, Acknowledgment ack) {
    var context = TransferContext.builder()
        .transactionId(event.transactionId())
        .build();

    chain.executeStep(creditStep, context);

    publisher.publish(event.withStatus(TransactionStatus.COMPLETED.name()));
    ack.acknowledge();
  }

  @DltHandler
  public void dlt(
      TransferEvent event,
      @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
      Acknowledgment ack
  ) {
    log.error("DLT: credit failed for transactionId={}, reason={}", event.transactionId(), errorMessage);

    var context = TransferContext.builder()
        .transactionId(event.transactionId())
        .build();

    try {
      // compensa em reverso: credit → reserveBalance → authorize(no-op) → validate
      chain.compensateFrom(creditStep, context, new RuntimeException(errorMessage));
    } catch (Exception e) {
      log.error("Compensation failed for transactionId={}", event.transactionId(), e);
    }

    publisher.publish(event
        .withStatus(TransactionStatus.FAILED.name())
        .withStatusDetails(errorMessage));
    ack.acknowledge();
  }
}
