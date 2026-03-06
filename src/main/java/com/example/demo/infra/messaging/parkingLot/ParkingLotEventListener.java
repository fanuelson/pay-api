package com.example.demo.infra.messaging.parkingLot;

import com.example.demo.domain.parkingLot.model.event.VehicleEnteredEvent;
import com.example.demo.domain.parkingLot.model.event.VehicleExitedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
  topics = "parking-lot.events",
  containerFactory = "listenerContainerFactory",
  groupId = "parking-lot-occupancy-group"
)
public class ParkingLotEventListener {

  @KafkaHandler
  public void handle(VehicleEnteredEvent event, Acknowledgment ack) {
    log.info("Vehicle entered parkingLotId={}, ticketId={}", event.parkingLotId(), event.ticketId());
    ack.acknowledge();
  }

  @KafkaHandler
  public void handle(VehicleExitedEvent event, Acknowledgment ack) {
    log.info("Vehicle exited parkingLotId={}, ticketId={}", event.parkingLotId(), event.ticketId());
    ack.acknowledge();
  }

  @KafkaHandler(isDefault = true)
  public void onUnknown(Object event, Acknowledgment ack) {
    log.debug("Ignored parking-lot event: {}", event.getClass().getSimpleName());
    ack.acknowledge();
  }
}
