package com.example.demo.application.exception;

public class NotificationException extends ApplicationException{
  protected NotificationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  public static NotificationException create() {
    return new NotificationException("Notification failed", null);
  }
}
