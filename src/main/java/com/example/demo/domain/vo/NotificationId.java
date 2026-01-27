package com.example.demo.domain.vo;

import static java.util.Objects.requireNonNull;

public record NotificationId(String value) {

  public NotificationId {
    requireNonNull(value);
  }

  public static NotificationId of(String value) {
    return new NotificationId(value);
  }

}
