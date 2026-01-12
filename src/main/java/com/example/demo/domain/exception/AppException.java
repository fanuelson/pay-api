package com.example.demo.domain.exception;

public class AppException extends RuntimeException {

  public AppException(String message, Throwable t) {
    super(message, t);
  }

  public AppException(String message) {
    super(message);
  }

  public AppException() {
    super();
  }
}
