package com.example.demo.domain.port.service;

public interface NotificationService {
  boolean sendNotification(Long userId, String email, String message);
}
