package com.example.demo.domain.parkingLot.repository;

import com.example.demo.domain.parkingLot.model.ParkingLotTicket;
import java.util.Optional;

public interface ParkingLotTicketRepository {
  void save(ParkingLotTicket ticket);
  Optional<ParkingLotTicket> findById(String id);
}
