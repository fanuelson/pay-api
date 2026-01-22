package com.example.demo.application.handler;

public class TransactionBalanceReservedEvent extends TransactionEvent {

  public TransactionBalanceReservedEvent(TransactionEvent other) {
    super(other);
  }

  public static TransactionBalanceReservedEvent from(TransactionEvent other) {
    return new TransactionBalanceReservedEvent(other);
  }

}
