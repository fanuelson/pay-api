package com.example.demo.application.usecase;

import com.example.demo.application.port.in.SendNotificationCommand;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.repository.NotificationEventRepository;
import com.example.demo.domain.notification.repository.NotificationRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {

  private final NotificationGateway notificationGateway;
  private final NotificationRepository notificationRepository;
  private final NotificationEventRepository notificationEventRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  @Timed("notification.send")
  public void execute(final SendNotificationCommand command) {
    Notification notification = notificationRepository
      .findById(command.notificationId())
      .orElseThrow();

    notificationGateway.send(notification);
    notificationEventRepository.findByNotificationId(notification.getId())
      .map(NotificationEvent::sent)
      .map(notificationEventRepository::save)
      .ifPresent(notificationEventPublisher::publish);
    log.info("Notification sent successfully: id={}, channel={}",
      notification.getId(), notification.getChannel());
  }

  public void onRetry(final NotificationEvent event) {
    notificationEventRepository.findByNotificationId(event.getNotificationId())
      .map(NotificationEvent::attempt)
      .map(notificationEventRepository::save);
  }

  public void onFail(final NotificationEvent event, final String cause) {
    notificationEventRepository.findByNotificationId(event.getNotificationId())
      .map(it -> it.failed(cause))
      .map(notificationEventRepository::save);
  }

}
