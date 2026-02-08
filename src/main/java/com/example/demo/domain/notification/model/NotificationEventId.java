package com.example.demo.domain.notification.model;

import java.util.UUID;
import static java.util.Objects.requireNonNull;

public record NotificationEventId(String value) {

  public NotificationEventId {
    requireNonNull(value);
  }

  public static NotificationEventId of(String value) {
    return new NotificationEventId(value);
  }

  public static NotificationEventId generate() {
    return new NotificationEventId(UUID.randomUUID().toString());
  }

}
