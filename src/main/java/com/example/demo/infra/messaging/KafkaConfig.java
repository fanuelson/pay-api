package com.example.demo.infra.messaging;

import com.example.demo.application.port.out.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final KafkaProperties kafkaProperties;

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
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }
}
