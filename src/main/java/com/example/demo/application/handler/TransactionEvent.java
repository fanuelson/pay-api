package com.example.demo.application.handler;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;
import java.util.function.Consumer;

@Getter
public abstract class TransactionEvent {

  private final String key;
  private final TransactionId transactionId;

  protected TransactionEvent(String key, TransactionId transactionId) {
    this.key = key;
    this.transactionId = transactionId;
  }

  protected TransactionEvent(TransactionEvent other) {
    this.key = other.getKey();
    this.transactionId = other.getTransactionId();
  }

  public void publish(Consumer<TransactionEvent> publisher) {
    publisher.accept(this);
  }
}
