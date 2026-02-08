package com.example.demo.application.port.out.gateway;

import com.example.demo.domain.notification.model.Notification;

public interface NotificationGateway {
  NotificationResult send(Notification notification);
}
