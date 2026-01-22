package com.example.demo.infra.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducerListener<T> implements ProducerListener<String, T> {

  @Override
  public void onSuccess(ProducerRecord<String, T> producerRecord, RecordMetadata recordMetadata) {
    log.debug("Published: [{}], partition={}, offset={}",
      producerRecord.value(), recordMetadata.partition(), recordMetadata.offset());
  }

  @Override
  public void onError(
    ProducerRecord<String, T> producerRecord, @Nullable RecordMetadata recordMetadata, @NonNull Exception exception) {
    log.error("Failed to publish event: [{}]", producerRecord.value(), exception);
  }
}
