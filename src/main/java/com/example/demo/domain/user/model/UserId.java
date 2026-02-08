package com.example.demo.domain.user.model;

import java.util.Objects;

public record UserId(String value) {

  public UserId {
    Objects.requireNonNull(value);
  }

  public static UserId of(String value) {
    return new UserId(value);
  }

  public static UserId of(Long value) {
    return new UserId(value.toString());
  }

  public Long asLong() {
    return Long.valueOf(value);
  }
}