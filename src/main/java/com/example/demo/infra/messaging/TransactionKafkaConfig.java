package com.example.demo.infra.messaging;

import com.example.demo.application.handler.TransactionEvent;
import com.example.demo.infra.messaging.consumer.LoggingInterceptor;
import com.example.demo.infra.messaging.publisher.KafkaProducerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
@RequiredArgsConstructor
public class TransactionKafkaConfig extends KafkaConfig {

  private final KafkaProperties properties;

  @Bean
  public ProducerFactory<String, TransactionEvent> transactionProducerFactory() {
    return producerFactory(properties);
  }

  @Bean
  public KafkaTemplate<String, TransactionEvent> transactionKafkaTemplate(
    KafkaProducerListener<TransactionEvent> producerListener
  ) {
    final var template = kafkaTemplate(transactionProducerFactory());
    template.setProducerListener(producerListener);
    return template;
  }

  @Bean
  public ConsumerFactory<String, TransactionEvent> transactionConsumerFactory() {
    return consumerFactory(properties, TransactionEvent.class);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> transactionListenerContainerFactory(
    LoggingInterceptor<TransactionEvent> loggingInterceptor
  ) {
    final var listenerContainerFactory = listenerContainerFactory(transactionConsumerFactory());
    listenerContainerFactory.setRecordInterceptor(loggingInterceptor);
    return listenerContainerFactory;
  }
}
