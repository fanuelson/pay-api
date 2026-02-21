package com.example.demo.application.notification;

import com.example.demo.application.notification.event.CreateNotificationEvent;
import com.example.demo.application.notification.event.NotificationCreatedEvent;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationUseCase implements NotificationEventHandler<CreateNotificationEvent> {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationEventPublisher publisher;

  @Override
  public void handle(CreateNotificationEvent event) {
    final var user = userRepository
      .findById(event.getUserId())
      .orElseThrow();
    user.getEnabledNotificationChannels()
      .stream()
      .map(Notification.fromChannel(user.getEmail(), event.getMessage()))
      .map(notificationRepository::save)
      .map(it -> NotificationEvent.requested(it.getId()))
      .forEach(it -> publisher.publish(NotificationCreatedEvent.from(it.getNotificationId())));
  }
}
