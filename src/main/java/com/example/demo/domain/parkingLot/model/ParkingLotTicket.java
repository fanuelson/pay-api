package com.example.demo.domain.parkingLot.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ParkingLotTicket {
  private String id;
  private String parkingLotId;
  private ParkingLotTicketDetail detail;
  private LocalDateTime createdAt;
}
