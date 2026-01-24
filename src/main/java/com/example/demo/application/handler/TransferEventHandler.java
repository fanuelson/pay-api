package com.example.demo.application.handler;

import com.example.demo.application.handler.processor.TransferEventProcessor;
import com.example.demo.application.handler.processor.TransferProcessorContext;
import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.repository.TransactionAggregateRepository;
import com.example.demo.infra.messaging.transfer.TransferEventKafkaPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventHandler {

  private final TransactionAggregateRepository transactionAggregateRepository;
  private final TransferEventKafkaPublisher transferEventPublisher;
  private final List<TransferEventProcessor> processors;

  private Map<TransactionEventType, TransferEventProcessor> processorMap;

  @PostConstruct
  void init() {
    processorMap = processors.stream()
      .collect(Collectors.toMap(
        TransferEventProcessor::getEventType,
        Function.identity()
      ));
    log.info("Registered {} transfer event processors: {}",
      processorMap.size(),
      processorMap.keySet());
  }

  public void handle(TransferEvent event) {
    final var transactionId = event.getTransactionId();
    final var transactionAggregate = transactionAggregateRepository.findById(transactionId);
    final var context = TransferProcessorContext.of(event, transactionAggregate);

    final var processor = processorMap.get(event.getType());
    if (processor == null) {
      log.warn("No processor found for event type: {}", event.getType());
      return;
    }

    log.debug("Processing event type {} with {}", event.getType(), processor.getClass().getSimpleName());

    processor.process(context)
      .ifPresent(transferEventPublisher::publish);
  }

  public TransactionEventType getErrorTypeFor(TransactionEventType eventType) {
    final var processor = processorMap.get(eventType);
    if (processor == null) {
      return TransactionEventType.FAILED;
    }
    return processor.getErrorType();
  }
}
