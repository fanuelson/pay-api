package com.example.demo.application.parkingLot;

import com.example.demo.domain.parkingLot.model.event.ParkingLotEvent;

public interface ParkingLotEventPublisher {
  void publish(ParkingLotEvent event);
}
