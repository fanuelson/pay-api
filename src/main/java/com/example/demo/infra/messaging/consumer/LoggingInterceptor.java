package com.example.demo.infra.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jspecify.annotations.Nullable;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingInterceptor<T> implements RecordInterceptor<String, T> {

  @Override
  public @Nullable ConsumerRecord<String, T> intercept(ConsumerRecord<String, T> record, Consumer<String, T> consumer) {
    log.info("Received event: [{}], partition: [{}], offset: [{}]",
      record.value(), record.partition(), record.offset());
    return record;
  }
}
