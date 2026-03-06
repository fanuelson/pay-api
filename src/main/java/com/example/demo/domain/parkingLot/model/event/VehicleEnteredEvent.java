package com.example.demo.domain.parkingLot.model.event;

import java.time.LocalDateTime;

public record VehicleEnteredEvent(
  String parkingLotId,
  String ticketId,
  String vehiclePlate,
  LocalDateTime enteredAt
) implements ParkingLotEvent {

  @Override
  public ParkingLotEventType type() {
    return ParkingLotEventType.VEHICLE_ENTERED;
  }
}
