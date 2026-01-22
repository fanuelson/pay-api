package com.example.demo.application.exception;

public class NotificationMaxAttemptsReachedException extends NotificationException {

  protected NotificationMaxAttemptsReachedException(String msg) {
    super(msg, null);
  }

  public static NotificationMaxAttemptsReachedException create() {
    return new NotificationMaxAttemptsReachedException("Max attempts reached");
  }
}
