package com.example.demo.application.usecase;

import com.example.demo.application.exception.NotificationException;
import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationStatus;
import com.example.demo.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {

  private final NotificationGateway notificationGateway;
  private final NotificationRepository notificationRepository;

  public void execute(final SendNotificationCommand command) {
    Notification notification = notificationRepository
      .findById(command.notificationId())
      .orElseThrow();

    if (notification.is(NotificationStatus.SENT)) {
      return;
    }

    try {
      notification.increaseAttempts();
      notificationGateway.send(notification);
      notification.sent();
      log.info("Notification sent successfully: id={}, channel={}",
        notification.getId(), notification.getChannel());
    } catch (Exception ex) {
      notification.failed(ex.getMessage());
      throw NotificationException.of(ex);
    } finally {
      notificationRepository.update(notification);
    }
  }

}
