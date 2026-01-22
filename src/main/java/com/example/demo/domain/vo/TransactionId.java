package com.example.demo.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(String value) {

  public TransactionId {
    Objects.requireNonNull(value);
  }

  public static TransactionId of(String value) {
    return new TransactionId(value);
  }

  public static TransactionId generate() {
    return of(UUID.randomUUID().toString());
  }

}
