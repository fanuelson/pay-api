package com.example.demo.domain.event;

import java.util.Objects;

public record TransactionEventRecord(TransactionEventKey key, TransactionEvent event) {

  public TransactionEventRecord {
    Objects.requireNonNull(key);
    Objects.requireNonNull(event);
  }

  public static TransactionEventRecord of(TransactionEventKey key, TransactionEvent event) {
    return new TransactionEventRecord(key, event);
  }

  public TransactionEventRecord withEvent(TransactionEvent event) {
    return new TransactionEventRecord(key, event);
  }
}
