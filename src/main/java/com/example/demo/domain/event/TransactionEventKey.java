package com.example.demo.domain.event;

import java.util.Objects;

public record TransactionEventKey(String key) {

  public TransactionEventKey {
    Objects.requireNonNull(key);
  }

  public static TransactionEventKey of(String key) {
    return new TransactionEventKey(key);
  }
}
