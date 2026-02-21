package com.example.demo.infra.messaging;

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
public class DefaultKafkaConfig extends KafkaConfig {


  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, Object> kafkaProducerFactory() {
    return producerFactory(kafkaProperties);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return kafkaTemplate(kafkaProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, Object> consumerFactory() {
    return consumerFactory(kafkaProperties, Object.class);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Object> listenerContainerFactory() {
    final var listenerContainerFactory = listenerContainerFactory(consumerFactory());
    listenerContainerFactory.setConcurrency(5);
    return listenerContainerFactory;
  }
}
