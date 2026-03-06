package com.example.demo.application.parkingLot.usecase;

import com.example.demo.application.parkingLot.EnterParkingLot;
import com.example.demo.application.parkingLot.ParkingLotEventPublisher;
import com.example.demo.domain.parkingLot.model.event.ParkingLotFullEvent;
import com.example.demo.domain.parkingLot.model.ParkingLotTicket;
import com.example.demo.domain.parkingLot.model.ParkingLotTicketDetail;
import com.example.demo.domain.parkingLot.model.event.VehicleEnteredEvent;
import com.example.demo.domain.parkingLot.repository.ParkingLotRepository;
import com.example.demo.domain.parkingLot.repository.ParkingLotTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnterParkingLotUseCase implements EnterParkingLot {

  private final ParkingLotRepository parkingLotRepository;
  private final ParkingLotTicketRepository parkingLotTicketRepository;
  private final ParkingLotEventPublisher eventPublisher;

  @Override
  @Transactional
  public String execute(String parkingLotId, String vehiclePlate) {
    final var parkingLot = parkingLotRepository.findById(parkingLotId).orElseThrow();

    if (parkingLot.getDetail().getOccupancy() >= parkingLot.getDetail().getMaxCapacity()) {
      throw new IllegalStateException("Parking lot full");
    }
    final var now = LocalDateTime.now();

    var detail = parkingLot.getDetail();
    detail.setOccupancy(detail.getOccupancy() + 1);
    parkingLotRepository.save(parkingLot);

    final var ticket = ParkingLotTicket.builder()
      .id(UUID.randomUUID().toString())
      .parkingLotId(parkingLotId)
      .createdAt(now)
      .detail(ParkingLotTicketDetail.builder().vehicleId(vehiclePlate).build())
      .build();

    parkingLotTicketRepository.save(ticket);

    if (detail.getOccupancy() >= detail.getMaxCapacity()) {
      eventPublisher.publish(new ParkingLotFullEvent(parkingLotId, detail.getMaxCapacity(), LocalDateTime.now()));
    }
    eventPublisher.publish(new VehicleEnteredEvent(parkingLotId, ticket.getId(), vehiclePlate, now));

    return ticket.getId();
  }
}
