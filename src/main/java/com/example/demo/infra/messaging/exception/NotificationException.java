package com.example.demo.infra.messaging.exception;

import com.example.demo.infra.exception.InfraException;

public class NotificationException extends InfraException {

  private NotificationException(String msg) {
    super(msg);
  }

  public static NotificationException msg(String message) {
    return new NotificationException("Notification failed: %s".formatted(message));
  }
}
