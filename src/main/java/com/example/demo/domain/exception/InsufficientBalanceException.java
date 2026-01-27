package com.example.demo.domain.exception;

public class InsufficientBalanceException extends BusinessException {

  public InsufficientBalanceException(String message) {
    super(message);
  }
}
