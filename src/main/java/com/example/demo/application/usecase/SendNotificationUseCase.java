package com.example.demo.application.usecase;


import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationEvent;
import com.example.demo.domain.model.NotificationStatus;
import com.example.demo.domain.repository.NotificationRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.application.port.out.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {

  private final NotificationService notificationService;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  @Transactional
  public void execute(final SendNotificationCommand command) {
    notificationRepository
      .findById(command.getNotificationId())
      .filter(it -> it.isNot(NotificationStatus.SENT))
      .ifPresent(this::send);
  }

  private void send(Notification notification) {
    try {
      throwing();
      final var user = userRepository
        .findById(notification.getRecipientId())
        .orElse(null);

      if (isNull(user)) {
        return;
      }
      boolean sent = notificationService
        .sendNotification(user.getId(), user.getEmail(), notification.getMessage());
      if (!sent) {
        throw new IllegalStateException("Circuit breaker open");
      }
      notification.sent();
      notificationRepository.update(notification);
      log.info("Notification sent successfully: id={}, channel={}",
        notification.getId(), notification.getChannel());
    } catch (Exception ex) {
      handleFailure(notification, ex);
    }
  }

  private void throwing() {
    throw new IllegalStateException("Circuit breaker open");
  }

  private void handleFailure(Notification notification, Exception ex) {
    notification.failed(ex.getMessage());
    notificationRepository.update(notification);
    if (notification.canRetry()) {
      notificationEventPublisher.publish(NotificationEvent.of(notification.getId()));
    }
  }

}
