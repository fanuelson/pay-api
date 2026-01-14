package com.example.demo.domain.port.service;

public interface NotificationService {
  void sendNotification(Long userId, String email, String message);
}
