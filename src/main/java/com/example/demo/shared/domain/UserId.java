package com.example.demo.shared.domain;

public record UserId(Long value) {
  public UserId {
  }

  public static UserId empty() {
    return new UserId(null);
  }

  public static UserId from(Long value) {
    return new UserId(value);
  }
}
