package com.example.demo.infra.messaging;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@EnableKafka
public abstract class KafkaConfig {

  public <T> ProducerFactory<String, T> producerFactory(KafkaProperties properties) {
    return new DefaultKafkaProducerFactory<>(
      properties.buildProducerProperties(),
      new StringSerializer(),
      new JacksonJsonSerializer<>()
    );
  }

  protected <T> KafkaTemplate<String, T> kafkaTemplate(ProducerFactory<String, T> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  protected <T> ConsumerFactory<String, T> consumerFactory(KafkaProperties properties, Class<T> type) {
    final var deserializer = new JacksonJsonDeserializer<>(type);
    deserializer.setUseTypeHeaders(true);
    deserializer.addTrustedPackages(type.getPackageName());
    return new DefaultKafkaConsumerFactory<>(
      properties.buildConsumerProperties(),
      new StringDeserializer(),
      deserializer
    );
  }

  protected <T> ConcurrentKafkaListenerContainerFactory<String, T> listenerContainerFactory(
    ConsumerFactory<String, T> consumerFactory
  ) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
    factory.setConsumerFactory(consumerFactory);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

}
