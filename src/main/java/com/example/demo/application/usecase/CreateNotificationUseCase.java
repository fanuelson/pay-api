package com.example.demo.application.usecase;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.domain.model.Notification;
import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
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
    final var userId = command.userId();
    final var transactionId = command.transactionId();
    final var msg = command.msg();

    final var user = userRepository.findById(userId).orElseThrow();

    user.getEnabledNotificationChannels()
      .stream()
      .map(Notification.fromChannel(transactionId, userId, msg))
      .map(notificationRepository::save)
      .map(NotificationEvent.fromNotification())
      .forEach(notificationEventPublisher::publish);

  }
}
