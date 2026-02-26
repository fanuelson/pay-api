package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import com.example.demo.domain.model.OutboxEvent;
import com.example.demo.domain.model.OutboxEventStatus;
import com.example.demo.domain.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class TransferOutboxPublisher implements TransferEventPublisher {

  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Value("${kafka.topics.transfer-events}")
  private String defaultTopic;

  private static final Map<String, String> STATUS_TO_TOPIC = Map.of(
      "PENDING",    "transfer.validate",
      "VALIDATED",  "transfer.authorize",
      "AUTHORIZED", "transfer.reserve",
      "RESERVED",   "transfer.credit",
      "COMPLETED",  "transfer.notify",
      "FAILED",     "transfer.failed"
  );

  @Override
  public void publish(TransferEvent event) {
    var topic = STATUS_TO_TOPIC.getOrDefault(event.status(), defaultTopic);
    try {
      var outboxEvent = OutboxEvent.builder()
          .aggregateId(event.transactionId())
          .eventKey(String.valueOf(event.payerId()))
          .aggregateType("TRANSFER")
          .eventType("TRANSFER_" + event.status())
          .topic(topic)
          .payload(objectMapper.writeValueAsString(event))
          .payloadType(TransferEvent.class.getName())
          .status(OutboxEventStatus.PENDING)
          .attempts(0)
          .maxAttempts(5)
          .createdAt(LocalDateTime.now())
          .build();

      outboxEventRepository.save(outboxEvent);
      log.debug("Outbox event saved for transactionId={}, status={}", event.transactionId(), event.status());
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize TransferEvent", e);
    }
  }
}
