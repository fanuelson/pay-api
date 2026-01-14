package com.example.demo.infra.messaging;

import com.example.demo.application.port.out.event.TransferEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;


@Configuration
@RequiredArgsConstructor
public class TransferKafkaConfig {

  @Autowired
  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, TransferEvent> transferProducerFactory() {
    return new DefaultKafkaProducerFactory<>(
      kafkaProperties.buildProducerProperties(),
      new StringSerializer(),
      new JacksonJsonSerializer<>()
    );
  }

  @Bean
  public KafkaTemplate<String, TransferEvent> transferKafkaTemplate() {
    return new KafkaTemplate<>(transferProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, TransferEvent> transferConsumerFactory() {
    var deserializer = new JacksonJsonDeserializer<>(TransferEvent.class);
    deserializer.addTrustedPackages("*");
    return new DefaultKafkaConsumerFactory<>(
      kafkaProperties.buildConsumerProperties(),
      new StringDeserializer(),
      deserializer
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TransferEvent> transferListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, TransferEvent>();
    factory.setConsumerFactory(transferConsumerFactory());
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }
}
