package com.example.demo.application.notification;

import com.example.demo.application.notification.event.NotificationEvent;

public interface NotificationEventHandler<T extends NotificationEvent> {
  void handle(T event);
}
