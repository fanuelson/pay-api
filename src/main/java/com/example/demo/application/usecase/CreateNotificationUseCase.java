package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.repository.NotificationEventRepository;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.user.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationUseCase {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationEventRepository notificationEventRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  @Timed("notification.create")
  public void execute(final CreateNotificationCommand command) {
    final var user = userRepository
      .findById(command.userId())
      .orElseThrow();

    user.getEnabledNotificationChannels()
      .stream()
      .map(Notification.fromChannel(user.getEmail(), command.msg()))
      .map(notificationRepository::save)
      .map(it -> NotificationEvent.requested(it.getId()))
      .map(notificationEventRepository::save)
      .forEach(notificationEventPublisher::publish);

  }
}
