package com.example.demo.infra.messaging.listener;

import com.example.demo.domain.model.TransferEvent;
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
    log.info("Received TransferEvent: transactionId={}, status={}, partition={}, offset={}",
      event.transactionId(), event.status(), partition, offset);

    try {
      processTransferEvent(event);
      ack.acknowledge();
      log.debug("TransferEvent processed successfully: transactionId={}", event.transactionId());
    } catch (Exception e) {
      log.error("Error processing TransferEvent: transactionId={}", event.transactionId(), e);
      // Não faz ack - mensagem será reprocessada
    }
  }

  private void processTransferEvent(TransferEvent event) {
    // TODO: Implementar lógica de processamento
    // Exemplo: atualizar status da transação, notificar sistemas externos, etc.
  }
}
