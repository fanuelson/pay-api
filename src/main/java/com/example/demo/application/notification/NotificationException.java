package com.example.demo.application.notification;

import com.example.demo.application.exception.ApplicationException;
import static com.example.demo.application.helper.StringHelper.prependIfMissing;

public class NotificationException extends ApplicationException {
  protected NotificationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  public static NotificationException of(String msg, Throwable t) {
    final var resultMsg = prependIfMissing(msg, ": ");
    return new NotificationException("Notification failed" + resultMsg, t);
  }

  public static NotificationException of(String msg) {
    return of(msg, null);
  }

  public static NotificationException of(Throwable t) {
    return of(t.getMessage(), t);
  }
}
