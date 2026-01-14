package com.example.demo.infra.messaging.listener;

import com.example.demo.domain.model.NotificationEvent;
import com.example.demo.domain.port.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventDltListener {

  private final NotificationRepository notificationRepository;

  @KafkaListener(
    topics = "${kafka.dlt.topic}",
    containerFactory = "dltListenerContainerFactory"
  )
  public void onDltEvent(
    @Payload NotificationEvent event,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
    @Header(KafkaHeaders.OFFSET) long offset,
    @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage
  ) {
    final var notificationId = event.getNotificationId();
    log.error("DLT received - notificationId={}, partition={}, offset={}, error={}",
      notificationId, partition, offset, exceptionMessage);

    notificationRepository.findById(notificationId)
      .ifPresent(notification -> {
        notification.failed("DLT: " + exceptionMessage);
        notificationRepository.update(notification.getId(), notification);
        log.info("Notification marked as FAILED in database - notificationId={}", notificationId);
      });
  }
}
