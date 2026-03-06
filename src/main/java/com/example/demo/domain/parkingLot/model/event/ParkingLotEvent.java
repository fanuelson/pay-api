package com.example.demo.domain.parkingLot.model.event;

public sealed interface ParkingLotEvent permits VehicleEnteredEvent, VehicleExitedEvent, ParkingLotFullEvent {
  String parkingLotId();
  ParkingLotEventType type();
}
