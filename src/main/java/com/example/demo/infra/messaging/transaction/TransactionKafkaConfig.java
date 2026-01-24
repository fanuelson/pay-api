package com.example.demo.infra.messaging.transaction;

import com.example.demo.domain.event.TransactionEvent;
import com.example.demo.infra.messaging.KafkaConfig;
import com.example.demo.infra.messaging.LoggingInterceptor;
import com.example.demo.infra.messaging.KafkaProducerListener;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
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

  @Bean
  public NewTopic transactionEventTopic(TransactionTopicProperties topicProperties) {
    return TopicBuilder.name(topicProperties.getName())
      .partitions(topicProperties.getPartitions())
      .replicas(topicProperties.getReplicas())
      .build();

  }

  @Bean
  public NewTopic transactionEventRetryTopic(TransactionTopicProperties topicProperties) {
    return TopicBuilder.name(topicProperties.getRetryName())
      .partitions(topicProperties.getPartitions())
      .replicas(topicProperties.getReplicas())
      .build();
  }
}
