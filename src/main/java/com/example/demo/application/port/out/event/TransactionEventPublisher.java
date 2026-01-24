package com.example.demo.application.port.out.event;

import com.example.demo.domain.event.TransactionEvent;
import com.example.demo.domain.event.TransactionEventRecord;

public interface TransactionEventPublisher extends EventPublisher<TransactionEvent> {
  void publish(TransactionEventRecord event);

}
