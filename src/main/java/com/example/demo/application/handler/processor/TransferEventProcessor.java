package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import java.util.Optional;

public interface TransferEventProcessor {

  TransactionEventType getEventType();

  Optional<TransferEvent> process(TransferProcessorContext context);

  default TransactionEventType getErrorType() {
    return TransactionEventType.FAILED;
  }
}
