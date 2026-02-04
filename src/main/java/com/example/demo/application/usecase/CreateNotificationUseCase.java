package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationUseCase {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  public void execute(final CreateNotificationCommand command) {
    final var user = userRepository
      .findById(command.userId())
      .orElseThrow();

    user.getEnabledNotificationChannels()
      .stream()
      .map(
        Notification.fromChannel(
          command.userId().asLong(),
          user.getEmail(),
          command.msg()
        )
      )
      .map(notificationRepository::save)
      .map(it -> NotificationEvent.request(it.getId()))
      .forEach(notificationEventPublisher::publish);

  }
}
