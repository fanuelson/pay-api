package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditedEventProcessor implements TransferEventProcessor {

  private final TransactionRepository transactionRepository;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.CREDITED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    final var transaction = context.aggregate().complete();
    transactionRepository.save(transaction);

    return Optional.of(context.event().to(TransactionEventType.COMPLETED));
  }
}
