package com.example.demo.infra.messaging.transfer;

import com.example.demo.application.handler.TransferEventHandler;
import com.example.demo.domain.event.TransferEvent;
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
  concurrency = "#{@transferTopicProperties.partitions}",
  backOff = @BackOff(delayString = "#{@transferTopicProperties.delay}ms"),
  attempts = "#{@transferTopicProperties.maxAttempts}",
  numPartitions = "#{@transferTopicProperties.partitions}",
  listenerContainerFactory = "transferListenerContainerFactory",
  kafkaTemplate = "transferKafkaTemplate",
  dltStrategy = DltStrategy.FAIL_ON_ERROR
)
@KafkaListener(
  topics = "#{@transferTopicProperties.name}",
  containerFactory = "transferListenerContainerFactory"
)
public class TransferEventConsumer {

  private final TransferEventHandler transferEventHandler;

  @KafkaHandler
  public void handle(@Payload TransferEvent event, Acknowledgment ack) {
    log.info("Processing transfer event: type={}, transactionId={}",
      event.getType(), event.getTransactionId());

    transferEventHandler.handle(event);
    ack.acknowledge();

    log.debug("Transfer event processed successfully: type={}, transactionId={}",
      event.getType(), event.getTransactionId());
  }

  @KafkaHandler(isDefault = true)
  public void onDefault(Object unknown, Acknowledgment ack) {
    log.warn("Received unknown event type, discarding: {}", unknown);
    ack.acknowledge();
  }

  @DltHandler
  public void handleDlt(
    @Payload TransferEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    log.error("DLT received failed event: type={}, transactionId={}, error={}",
      event.getType(), event.getTransactionId(), errorMessage);

    try {
      final var errorType = transferEventHandler.getErrorTypeFor(event.getType());
      final var errorEvent = event.withCause(errorMessage).to(errorType);

      log.info("Processing compensation event: type={}, transactionId={}",
        errorEvent.getType(), errorEvent.getTransactionId());

      transferEventHandler.handle(errorEvent);

      log.info("Compensation completed successfully: transactionId={}",
        event.getTransactionId());
    } catch (Exception e) {
      log.error("Failed to process compensation for transactionId={}, manual intervention required",
        event.getTransactionId(), e);
    } finally {
      ack.acknowledge();
    }
  }
}
