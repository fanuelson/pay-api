package com.example.demo.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(String value) {
  public TransactionId {
    Objects.requireNonNull(value, "valueId não pode ser nulo");
  }

  public static TransactionId create() {
    return new TransactionId(UUID.randomUUID().toString());
  }
}
