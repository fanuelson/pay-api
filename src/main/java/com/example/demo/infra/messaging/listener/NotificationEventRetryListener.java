package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.out.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.listener.RetryListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventRetryListener implements RetryListener {

  @Override
  public void failedDelivery(
    ConsumerRecord<?, ?> record,
    @Nullable Exception ex,
    int deliveryAttempt) {
    log.info("Received failed delivery, attempt: {}", deliveryAttempt);
    final var event = (NotificationEvent) record.value();
    log.info("Notification Event: {}", event);
  }

}
