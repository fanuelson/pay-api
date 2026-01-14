package com.example.demo.application.port.out.service;

public interface NotificationService {
  boolean sendNotification(Long userId, String email, String message);
}
