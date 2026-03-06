package com.example.demo.domain.parkingLot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingLotDetail {

  private int occupancy;
  private int maxCapacity;

}
