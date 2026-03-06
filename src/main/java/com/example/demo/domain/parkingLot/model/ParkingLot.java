package com.example.demo.domain.parkingLot.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ParkingLot {

  private String id;
  private ParkingLotDetail detail;
  private LocalDateTime createdAt;

}
