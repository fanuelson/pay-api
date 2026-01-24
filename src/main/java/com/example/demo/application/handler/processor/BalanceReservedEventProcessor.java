package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class BalanceReservedEventProcessor implements TransferEventProcessor {

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.BALANCE_RESERVED;
  }


  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    return Optional.of(context.event().to(TransactionEventType.AUTHORIZATION_REQUESTED));
  }
}
