package com.example.demo.domain.vo;

import java.util.Objects;

public record NotificationAddress(
  NotificationAddressType type,
  String value
) {

  public NotificationAddress {
    Objects.requireNonNull(type);
    Objects.requireNonNull(value);
  }

  public static NotificationAddress of(NotificationAddressType type, String value) {
    return new NotificationAddress(type, value);
  }
}
