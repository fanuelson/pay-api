package com.example.demo.infra.messaging;

import com.example.demo.domain.model.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  @Autowired
  private final KafkaProperties kafkaProperties;

  @Value("${kafka.dlt.topic}")
  private String dltTopic;

  @Value("${kafka.dlt.retry.max-attempts}")
  private int retryMaxAttempts;

  @Value("${kafka.dlt.retry.backoff-ms}")
  private long retryBackoffMs;

  @Value("${kafka.dlt.dlt-retry.max-attempts}")
  private int dltRetryMaxAttempts;

  @Value("${kafka.dlt.dlt-retry.backoff-ms}")
  private long dltRetryBackoffMs;

  // ==================== NOTIFICATION ====================

  @Bean
  public ProducerFactory<String, NotificationEvent> notificationProducerFactory() {
    return new DefaultKafkaProducerFactory<>(
      kafkaProperties.buildProducerProperties(),
      new StringSerializer(),
      new JacksonJsonSerializer<>()
    );
  }

  @Bean
  public KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate() {
    return new KafkaTemplate<>(notificationProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, NotificationEvent> notificationConsumerFactory() {
    var deserializer = new JacksonJsonDeserializer<>(NotificationEvent.class);
    deserializer.addTrustedPackages("*");
    return new DefaultKafkaConsumerFactory<>(
      kafkaProperties.buildConsumerProperties(),
      new StringDeserializer(),
      deserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> notificationListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>();
    factory.setConsumerFactory(notificationConsumerFactory());
    factory.setCommonErrorHandler(notificationErrorHandler());
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> dltListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>();
    factory.setConsumerFactory(notificationConsumerFactory());
    factory.setConcurrency(1);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
    factory.setCommonErrorHandler(dltErrorHandler());
    return factory;
  }

  // ==================== ERROR HANDLERS (Notification DLT) ====================

  private CommonErrorHandler notificationErrorHandler() {
    var recoverer = new DeadLetterPublishingRecoverer(notificationKafkaTemplate(),
      (record, ex) -> {
        log.error("Sending to DLT: key={}, error={}", record.key(), ex.getMessage());
        return new TopicPartition(dltTopic, record.partition());
      });

    var backOff = new FixedBackOff(retryBackoffMs, retryMaxAttempts - 1);
    var handler = new DefaultErrorHandler(recoverer, backOff);

    handler.setRetryListeners((record, ex, attempt) ->
      log.warn("Notification retry {}/{} - key={}", attempt, retryMaxAttempts, record.key()));

    return handler;
  }

  private CommonErrorHandler dltErrorHandler() {
    var backOff = new FixedBackOff(dltRetryBackoffMs, dltRetryMaxAttempts - 1);

    var handler = new DefaultErrorHandler((record, ex) ->
      log.error("DLT failed after {} retries - key={}. Manual intervention required.",
        dltRetryMaxAttempts, record.key(), ex),
      backOff);

    handler.setRetryListeners((record, ex, attempt) ->
      log.warn("DLT retry {}/{} - key={}", attempt, dltRetryMaxAttempts, record.key()));

    return handler;
  }
}
