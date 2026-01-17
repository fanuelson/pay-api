package com.example.demo.infra.messaging;

import com.example.demo.application.port.out.event.TransferEvent;
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
public class TransferKafkaConfig extends KafkaConfig {

  private final KafkaProperties properties;

  @Bean
  public ProducerFactory<String, TransferEvent> transferProducerFactory() {
    return producerFactory(properties);
  }

  @Bean
  public KafkaTemplate<String, TransferEvent> transferKafkaTemplate() {
    return kafkaTemplate(transferProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, TransferEvent> transferConsumerFactory() {
    return consumerFactory(properties, TransferEvent.class);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TransferEvent> transferListenerContainerFactory() {
    return listenerContainerFactory(transferConsumerFactory());
  }
}
