package com.example.demo.domain.parkingLot.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ParkingLotTicketDetail {
  private String vehicleId;
  private LocalDateTime exitAt;
}
