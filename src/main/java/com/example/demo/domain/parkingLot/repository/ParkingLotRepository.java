package com.example.demo.domain.parkingLot.repository;

import com.example.demo.domain.parkingLot.model.ParkingLot;
import java.util.Optional;

public interface ParkingLotRepository {
  Optional<ParkingLot> save(ParkingLot parkingLot);
  Optional<ParkingLot> findById(String id);
}
