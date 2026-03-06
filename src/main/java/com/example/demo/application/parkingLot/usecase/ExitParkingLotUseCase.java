package com.example.demo.application.parkingLot.usecase;

import com.example.demo.application.parkingLot.ExitParkingLot;
import com.example.demo.application.parkingLot.ParkingLotEventPublisher;
import com.example.demo.domain.parkingLot.model.event.VehicleExitedEvent;
import com.example.demo.domain.parkingLot.repository.ParkingLotRepository;
import com.example.demo.domain.parkingLot.repository.ParkingLotTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExitParkingLotUseCase implements ExitParkingLot {

  private final ParkingLotTicketRepository ticketRepository;
  private final ParkingLotRepository parkingLotRepository;
  private final ParkingLotEventPublisher eventPublisher;

  @Override
  @Transactional
  public void execute(String ticketId) {
    final var ticket = ticketRepository.findById(ticketId).orElseThrow();

    if (ticket.getDetail().getExitAt() != null) {
      throw new IllegalStateException("Ticket already used for exit: " + ticketId);
    }

    final var parkingLot = parkingLotRepository.findById(ticket.getParkingLotId()).orElseThrow();
    final var now = LocalDateTime.now();

    ticket.getDetail().setExitAt(now);
    ticketRepository.save(ticket);

    final var detail = parkingLot.getDetail();
    detail.setOccupancy(detail.getOccupancy() - 1);
    parkingLotRepository.save(parkingLot);

    eventPublisher.publish(new VehicleExitedEvent(
      ticket.getParkingLotId(),
      ticket.getId(),
      ticket.getDetail().getVehicleId(),
      now
    ));
  }
}
