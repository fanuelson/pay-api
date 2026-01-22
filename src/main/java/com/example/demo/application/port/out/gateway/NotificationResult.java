package com.example.demo.application.port.out.gateway;

public record NotificationResult(
  boolean sent
) {

  public static NotificationResult success() {
    return new NotificationResult(true);
  }
}
