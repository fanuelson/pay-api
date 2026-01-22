package com.example.demo.application.port.out.event;

public enum TransferEventType {
  REQUESTED,
  VALIDATED,
  AUTHORIZATION_REQUESTED,
  AUTHORIZED,
  AUTHORIZATION_FAILED,
  BALANCE_RESERVED,
  CREDITED,
  COMPLETED,
  FAILED
}
