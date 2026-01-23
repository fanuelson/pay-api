package com.example.demo.domain.vo;

import java.util.Objects;

public record UserId(String value) {

  public UserId {
    Objects.requireNonNull(value);
  }

  public static UserId of(String value) {
    return new UserId(value);
  }

  public Long asLong() {
    return Long.valueOf(value);
  }
}