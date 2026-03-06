package com.example.demo.infra.messaging.parkingLot;

import com.example.demo.application.parkingLot.ParkingLotEventPublisher;
import com.example.demo.domain.model.OutboxEvent;
import com.example.demo.domain.model.OutboxEventStatus;
import com.example.demo.domain.parkingLot.model.event.ParkingLotEvent;
import com.example.demo.domain.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParkingLotOutboxPublisher implements ParkingLotEventPublisher {

  private static final String TOPIC = "parking-lot.events";
  private static final String AGGREGATE_TYPE = "PARKING_LOT";

  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void publish(ParkingLotEvent event) {
    try {
      var outboxEvent = OutboxEvent.builder()
        .aggregateId(event.parkingLotId())
        .aggregateType(AGGREGATE_TYPE)
        .eventType(event.type().name())
        .topic(TOPIC)
        .payload(objectMapper.writeValueAsString(event))
        .payloadType(event.getClass().getName())
        .status(OutboxEventStatus.PENDING)
        .attempts(0)
        .maxAttempts(5)
        .createdAt(LocalDateTime.now())
        .build();

      outboxEventRepository.save(outboxEvent);
      log.debug("Outbox event saved for parkingLotId={}, type={}", event.parkingLotId(), event.type());
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize ParkingLotEvent", e);
    }
  }
}
