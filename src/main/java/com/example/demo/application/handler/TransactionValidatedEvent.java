package com.example.demo.application.handler;

import lombok.Getter;


@Getter
public class TransactionValidatedEvent extends TransactionEvent {

  public TransactionValidatedEvent(TransactionEvent other) {
    super(other);
  }

  public static TransactionValidatedEvent from(TransactionEvent other) {
    return new TransactionValidatedEvent(other);
  }
}
