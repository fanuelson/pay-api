package com.example.demo.infra.messaging.transaction;

import com.example.demo.application.handler.TransactionEventHandler;
import com.example.demo.domain.event.TransactionAuthorizationFailedEvent;
import com.example.demo.domain.event.TransactionAuthorizationRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
@KafkaListener(
  topics = "#{@transactionTopicProperties.dltName}",
  containerFactory = "transactionListenerContainerFactory"
)
public class TransactionFailedEventConsumer {

  private final TransactionEventHandler transactionEventHandler;
  private final TransactionTopicProperties transactionTopicProperties;

  @KafkaHandler
  public void handle(
    @Payload TransactionAuthorizationRequestedEvent event,
    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
    Acknowledgment ack
  ) {
    final var authorizationFailedEvent = TransactionAuthorizationFailedEvent.from(event).withMsg(errorMessage);
    transactionEventHandler.handle(authorizationFailedEvent);
    ack.acknowledge();
  }


  @KafkaHandler(isDefault = true)
  public void onDefault(Object unknown) {
    log.warn("DLT error event without handler: [{}]", unknown);
  }

}
