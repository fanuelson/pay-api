package com.example.demo.application.notification;

import com.example.demo.application.notification.event.NotificationCompletedEvent;
import com.example.demo.application.notification.event.NotificationCreatedEvent;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotificationUseCase implements NotificationEventHandler<NotificationCreatedEvent> {

  private final NotificationRepository notificationRepository;
  private final NotificationEventPublisher publisher;
  private final NotificationGateway notificationGateway;

  public void handle(NotificationCreatedEvent event) {
    final var notificationId = event.getNotificationId();
    Notification notification = notificationRepository
      .findById(notificationId)
      .orElseThrow();
    notificationGateway.send(notification);
    publisher.publish(NotificationCompletedEvent.from(notificationId));
  }
}
