package com.example.demo.infra.messaging.transfer;

import com.example.demo.domain.event.TransferEvent;
import com.example.demo.infra.messaging.KafkaConfig;
import com.example.demo.infra.messaging.KafkaProducerListener;
import com.example.demo.infra.messaging.LoggingInterceptor;
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
public class TransferKafkaConfig extends KafkaConfig {

  private final KafkaProperties properties;

  @Bean
  public ProducerFactory<String, TransferEvent> transferProducerFactory() {
    return producerFactory(properties);
  }

  @Bean
  public KafkaTemplate<String, TransferEvent> transferKafkaTemplate(
    KafkaProducerListener<TransferEvent> producerListener
  ) {
    final var template = kafkaTemplate(transferProducerFactory());
    template.setProducerListener(producerListener);
    return template;
  }

  @Bean
  public ConsumerFactory<String, TransferEvent> transferConsumerFactory() {
    return consumerFactory(properties, TransferEvent.class);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, TransferEvent> transferListenerContainerFactory(
    LoggingInterceptor<TransferEvent> loggingInterceptor
  ) {
    final var listenerContainerFactory = listenerContainerFactory(transferConsumerFactory());
    listenerContainerFactory.setRecordInterceptor(loggingInterceptor);
    listenerContainerFactory.setConcurrency(5);
    return listenerContainerFactory;
  }

  @Bean
  public NewTopic transferEventTopic(TransferTopicProperties topicProperties) {
    return TopicBuilder.name(topicProperties.getName())
      .partitions(topicProperties.getPartitions())
      .replicas(topicProperties.getReplicas())
      .build();

  }

  @Bean
  public NewTopic transferEventRetryTopic(TransferTopicProperties topicProperties) {
    return TopicBuilder.name(topicProperties.getRetryName())
      .partitions(topicProperties.getPartitions())
      .replicas(topicProperties.getReplicas())
      .build();
  }

  @Bean
  public NewTopic transferEventDltTopic(TransferTopicProperties topicProperties) {
    return TopicBuilder.name(topicProperties.getDltName())
      .partitions(topicProperties.getPartitions())
      .replicas(topicProperties.getReplicas())
      .build();
  }
}
