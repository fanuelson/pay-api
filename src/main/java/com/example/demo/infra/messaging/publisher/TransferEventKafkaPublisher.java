package com.example.demo.infra.messaging.publisher;

import com.example.demo.application.port.out.event.TransferEvent;
import com.example.demo.application.port.out.event.TransferEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.function.BiConsumer;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class TransferEventKafkaPublisher implements TransferEventPublisher {

  @Autowired
  @Qualifier("transferKafkaTemplate")
  private KafkaTemplate<String, TransferEvent> kafkaTemplate;

  @Value("${app.kafka.topics.transfer-events.name}")
  private String topic;

  @Override
  public void publish(TransferEvent event) {
    logInit(event);
    kafkaTemplate
      .send(topic, String.valueOf(event.payerId()), event)
      .whenComplete(handleComplete(event));
  }

  private BiConsumer<SendResult<String, TransferEvent>, Throwable> handleComplete(TransferEvent event) {
    return (result, ex) -> {
      if (nonNull(ex)) {
        logError(event, ex);
      } else {
        logPublished(event, result);
      }
    };
  }

  private void logInit(TransferEvent event) {
    log.debug("Publishing TransferEvent: transactionId={}, status={}", event.transactionId(), event.status());
  }

  private void logError(TransferEvent event, Throwable ex) {
    log.error("Failed to publish: transactionId={}", event.transactionId(), ex);
  }

  private void logPublished(TransferEvent event, SendResult<String, TransferEvent> result) {
    log.debug("Published: transactionId={}, partition={}, offset={}",
      event.transactionId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
  }
}
