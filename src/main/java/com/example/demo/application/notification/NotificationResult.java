package com.example.demo.application.notification;

public record NotificationResult(boolean sent) {
  public static NotificationResult success() {
    return new NotificationResult(true);
  }
}
