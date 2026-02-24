package com.example.demo.application.exceptions;

public class NotificationException extends RuntimeException {

  protected NotificationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  public static NotificationException of(String msg) {
    return new NotificationException(msg, null);
  }
}
