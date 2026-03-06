package com.example.demo.domain.parkingLot.model.event;

import java.time.LocalDateTime;

public record ParkingLotFullEvent(
  String parkingLotId,
  int capacity,
  LocalDateTime occurredAt
) implements ParkingLotEvent {

  @Override
  public ParkingLotEventType type() {
    return ParkingLotEventType.PARKING_LOT_FULL;
  }
}
