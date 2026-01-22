package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.in.ExecuteTransferCommand;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.usecase.ExecuteTransferUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventListener extends AbstractListener {

  private final ExecuteTransferUseCase executeTransfer;

  @KafkaListener(
    topics = "${app.kafka.topics.transfer-events.name}",
    containerFactory = "transferListenerContainerFactory"
  )
  public void handle(@Payload TransferEvent event, Acknowledgment ack) {
    try {
      final var command = ExecuteTransferCommand.of(event.transactionId());
      executeTransfer.execute(command);
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.transactionId(), e);
    }
  }
}
