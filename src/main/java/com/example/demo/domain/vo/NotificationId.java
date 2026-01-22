package com.example.demo.domain.vo;

import java.util.Objects;

public record NotificationId(String value) {

  public NotificationId {
    Objects.requireNonNull(value);
  }

  public static NotificationId of(String value) {
    return new NotificationId(value);
  }

}
