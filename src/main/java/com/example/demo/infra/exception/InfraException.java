package com.example.demo.infra.exception;

public class InfraException extends RuntimeException {

  public InfraException(String message, Throwable t) {
    super(message, t);
  }

  public InfraException(String message) {
    super(message);
  }

  public InfraException() {
    super();
  }
}
