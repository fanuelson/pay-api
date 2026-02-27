package com.example.demo.application.usecase;


import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.service.NotificationService;
import com.example.demo.domain.notification.*;
import com.example.demo.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
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
  private final NotificationEventPublisher notificationEventPublisher;

  @Transactional
  public void execute(final SendNotificationCommand command) {
    final var notificationId = command.getNotificationId();
    Notification notification = notificationRepository
      .findById(notificationId)
      .orElseThrow();
    final var user = userRepository
      .findById(notification.getRecipientId())
      .orElseThrow();

    notificationService
      .sendNotification(notification.getRecipientId(), user.getEmail(), notification.getMessage());

    notification.sent();
    notificationRepository.update(notification);
    notificationEventPublisher.publish(NotificationEvent.of(notificationId, notification.getStatus()));
  }

  public void handleFailure(Long notificationId, String reason) {
    Notification notification = notificationRepository
      .findById(notificationId)
      .orElseThrow();
    notification.failed();
    notificationRepository.update(notification);
    notificationEventPublisher.publish(NotificationFailedEvent.of(notificationId, reason));
  }

}
