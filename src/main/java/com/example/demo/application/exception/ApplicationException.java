package com.example.demo.application.exception;

public abstract class ApplicationException extends RuntimeException {

  protected ApplicationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  protected ApplicationException(String msg) {
    super(msg);
  }
}
