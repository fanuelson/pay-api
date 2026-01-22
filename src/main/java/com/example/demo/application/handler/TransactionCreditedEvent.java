package com.example.demo.application.handler;

import lombok.Getter;

@Getter
public class TransactionCreditedEvent extends TransactionEvent {

  public TransactionCreditedEvent(TransactionEvent other) {
    super(other);
  }

  public static TransactionCreditedEvent from(TransactionEvent other) {
    return new TransactionCreditedEvent(other);
  }
}
