package com.example.demo.application.usecase;

import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationEvent;
import com.example.demo.domain.port.event.NotificationEventPublisher;
import com.example.demo.domain.port.repository.NotificationRepository;
import com.example.demo.domain.port.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendTransferNotificationUseCase {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  public void execute(final String transactionId, final Long userId, final String msg) {
    final var user = userRepository.findById(userId).orElseThrow();

    user.getEnabledNotificationChannels()
      .stream()
      .map(it -> Notification.of(transactionId, userId, it, msg))
      .map(notificationRepository::save)
      .map(it -> NotificationEvent.of(it.getId()))
      .forEach(notificationEventPublisher::publish);

  }
}
