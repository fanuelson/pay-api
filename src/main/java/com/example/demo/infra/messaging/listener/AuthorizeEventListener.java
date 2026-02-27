package com.example.demo.infra.messaging.listener;

import com.example.demo.application.chain.TransferChain;
import com.example.demo.application.chain.transfer.step.AuthorizeStep;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
  backOff = @BackOff(delayString = "5000ms"),
  attempts = "4",
  numPartitions = "5",
  concurrency = "5",
  exclude = {BusinessException.class, CallNotPermittedException.class},
  dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(
  topics = "transfer.validated",
  containerFactory = "listenerContainerFactory",
  groupId = "authorize-group"
)
public class AuthorizeEventListener {

  private final AuthorizeStep authorizeStep;
  private final TransferChain chain;
  private final TransferEventPublisher publisher;
  private final TransactionAggregateRepository repository;

  @KafkaHandler(isDefault = true)
  public void handle(TransferEvent event, Acknowledgment ack) {
    var context = repository.findById(event.transactionId());

    chain.executeStep(authorizeStep, context);

    publisher.publish(event
      .withStatus(TransactionStatus.AUTHORIZED.name())
      .withAuthorizationCode(context.getTransaction().getAuthorizationCode()));
    ack.acknowledge();
  }

  @DltHandler
  public void dlt(
    TransferEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    log.error("DLT: authorize failed for transactionId={}, reason={}", event.transactionId(), errorMessage);

    var context = repository.findById(event.transactionId());

    try {
      // compensa em reverso: authorize(no-op) → validate
      chain.compensateFrom(authorizeStep, context, new RuntimeException(errorMessage));
    } catch (Exception e) {
      log.error("Compensation failed for transactionId={}", event.transactionId(), e);
    }

    publisher.publish(event
      .withStatus(TransactionStatus.FAILED.name())
      .withStatusDetails(errorMessage));
    ack.acknowledge();
  }
}
