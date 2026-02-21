package com.example.demo.application.notification;

import com.example.demo.domain.notification.model.Notification;

public interface NotificationGateway {
  NotificationResult send(Notification notification);
}
