package com.example.demo.infra.messaging.outbox;

import com.example.demo.domain.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayPoller {

  private static final int BATCH_SIZE = 500;

  private final OutboxEventRepository outboxEventRepository;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Value("${outbox.dispatch-timeout-minutes:5}")
  private int dispatchTimeoutMinutes;

  @Scheduled(fixedDelayString = "${outbox.poll-interval-ms:5000}")
  public void poll() {
    dispatchPending();
    recoverStaleDispatched();
  }

  private void dispatchPending() {
    outboxEventRepository
      .findPending(BATCH_SIZE)
      .forEach(outboxEvent -> {
        try {
          var payloadClass = Class.forName(outboxEvent.getPayloadType());
          var event = objectMapper.readValue(outboxEvent.getPayload(), payloadClass);

          kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getEffectiveEventKey(), event).join();

          outboxEvent.dispatched();
          outboxEventRepository.update(outboxEvent);

          log.debug("Dispatched outboxEventId={}, type={}", outboxEvent.getId(), outboxEvent.getEventType());
        } catch (Exception e) {
          log.error("Failed to dispatch outboxEventId={}, type={}", outboxEvent.getId(), outboxEvent.getEventType(), e);

          outboxEvent.dispatched(); // incrementa attempts mesmo em falha
          if (outboxEvent.hasReachedMaxAttempts()) {
            outboxEvent.failed();
            log.error("OutboxEvent permanently failed: id={}", outboxEvent.getId());
          } else {
            outboxEvent.resetToPending();
          }
          outboxEventRepository.update(outboxEvent);
        }
    });
  }

  private void recoverStaleDispatched() {
    var threshold = LocalDateTime.now().minusMinutes(dispatchTimeoutMinutes);
    outboxEventRepository.findDispatchedBefore(threshold, BATCH_SIZE).forEach(outboxEvent -> {
      if (outboxEvent.hasReachedMaxAttempts()) {
        outboxEvent.failed();
        log.error("OutboxEvent permanently failed after timeout: id={}", outboxEvent.getId());
      } else {
        outboxEvent.resetToPending();
        log.warn("Recovered stale outboxEventId={}", outboxEvent.getId());
      }
      outboxEventRepository.update(outboxEvent);
    });
  }
}
