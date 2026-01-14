package com.example.demo.infra.messaging.listener;

import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationEvent;
import com.example.demo.domain.port.repository.NotificationRepository;
import com.example.demo.domain.port.repository.UserRepository;
import com.example.demo.domain.port.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @KafkaListener(
    topics = "${kafka.topics.notification-events}",
    containerFactory = "notificationListenerContainerFactory"
  )
  public void onNotificationEvent(
    @Payload NotificationEvent event,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
    @Header(KafkaHeaders.OFFSET) long offset,
    Acknowledgment ack
  ) {
    final var notificationId = event.getNotificationId();
    log.info("Received NotificationEvent: notificationId={}, partition={}, offset={}",
      notificationId, partition, offset);

    final var notification = notificationRepository.findById(notificationId)
      .orElseThrow(() -> {
        log.warn("Notification not found with id={}", notificationId);
        return new IllegalStateException("Notification not found: " + notificationId);
      });

    send(notification);
    ack.acknowledge();
    log.debug("NotificationEvent processed successfully: notificationId={}", notificationId);
  }

  private void send(Notification notification) {
    final var recipientId = notification.getRecipientId();
    final var msg = notification.getMessage();

    final var user = userRepository.findById(recipientId)
      .orElseThrow(() -> new IllegalStateException("User not found: " + recipientId));

    boolean sent = notificationService.sendNotification(user.getId(), user.getEmail(), msg);
    if (!sent) {
      throw new IllegalStateException("Notification failed - circuit breaker open");
    }

    notification.sent();
    notificationRepository.update(notification.getId(), notification);
  }
}
