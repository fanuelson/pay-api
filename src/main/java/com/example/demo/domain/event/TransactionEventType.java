package com.example.demo.domain.event;

public enum TransactionEventType {
  REQUESTED,
  VALIDATED,
  BALANCE_RESERVED,
  AUTHORIZATION_REQUESTED,
  AUTHORIZED,
  CREDITED,
  COMPLETED,

  AUTHORIZATION_FAILED,
  FAILED
}
