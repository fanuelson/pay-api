package com.example.demo.application.exception;

import static com.example.demo.domain.helper.StringHelper.prependIfMissing;

public class NotificationException extends ApplicationException {
  protected NotificationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  public static NotificationException create(String msg, Throwable t) {
    final var resultMsg = prependIfMissing(msg, ": ");
    return new NotificationException("Notification failed" + resultMsg, t);
  }

  public static NotificationException create(String msg) {
    return create(msg, null);
  }

  public static NotificationException create() {
    return create(null, null);
  }

  public static NotificationException create(Throwable t) {
    return create(null, t);
  }
}
