package com.example.demo.application.usecase;

import com.example.demo.application.exception.NotificationException;
import com.example.demo.application.exception.NotificationMaxAttemptsReachedException;
import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.service.NotificationService;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationStatus;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {

  private final NotificationService notificationService;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  public void execute(final SendNotificationCommand command) {
    Notification notification = notificationRepository
      .findById(command.notificationId())
      .orElseThrow();

    if (notification.is(NotificationStatus.SENT)) {
      return;
    }

    if (notification.hasReachedMaxAttempts()) {
      throw NotificationMaxAttemptsReachedException.create();
    }

    try {
      notification.increaseAttempts();
      send(notification);
      notification.sent();
      log.info("Notification sent successfully: id={}, channel={}",
        notification.getId(), notification.getChannel());
    } catch (Exception ex) {
      notification.failed(ex.getMessage());
      throw NotificationException.create(ex);
    } finally {
      notificationRepository.update(notification);
    }
  }

  private void send(Notification notification) {
    final var user = userRepository
      .findById(notification.getRecipientId())
      .orElseThrow();

    notificationService.sendNotification(
      user.getId(), user.getEmail(), notification.getMessage()
    );
  }

}
