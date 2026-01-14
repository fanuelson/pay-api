package com.example.demo.infra.messaging.listener;

import com.example.demo.application.port.in.ExecuteTransferCommand;
import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.usecase.ExecuteTransferUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventListener {

  private final ExecuteTransferUseCase executeTransferUseCase;

  @KafkaListener(
    topics = "${kafka.topics.transfer-events}",
    containerFactory = "transferListenerContainerFactory"
  )
  public void onTransferEvent(
    @Payload TransferEvent event,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
    @Header(KafkaHeaders.OFFSET) long offset,
    Acknowledgment ack
  ) {
    log.info("Received TransferEvent: transactionId={}, partition={}, offset={}",
      event.transactionId(), partition, offset);

    try {
      executeTransferUseCase.execute(new ExecuteTransferCommand(event.transactionId()));
      ack.acknowledge();
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.transactionId(), e);
    }
  }
}
