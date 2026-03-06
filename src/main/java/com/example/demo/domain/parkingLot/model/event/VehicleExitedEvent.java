package com.example.demo.domain.parkingLot.model.event;

import java.time.LocalDateTime;

public record VehicleExitedEvent(
  String parkingLotId,
  String ticketId,
  String vehiclePlate,
  LocalDateTime exitedAt
) implements ParkingLotEvent {

  @Override
  public ParkingLotEventType type() {
    return ParkingLotEventType.VEHICLE_EXITED;
  }
}
