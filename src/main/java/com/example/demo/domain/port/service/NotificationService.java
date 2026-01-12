package com.example.demo.domain.port.service;

public interface NotificationService {
  void notify(Long userId, String email, String message);
}
