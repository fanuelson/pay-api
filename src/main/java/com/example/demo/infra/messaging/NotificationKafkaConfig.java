package com.example.demo.infra.messaging;

import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.infra.messaging.listener.LoggingInterceptor;
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
public class NotificationKafkaConfig extends KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, NotificationEvent> notificationProducerFactory() {
    return producerFactory(kafkaProperties);
  }

  @Bean
  public KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate() {
    return kafkaTemplate(notificationProducerFactory());
  }

  @Bean
  public ConsumerFactory<String, NotificationEvent> notificationConsumerFactory() {
    return consumerFactory(kafkaProperties, NotificationEvent.class);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>
  notificationListenerContainerFactory(LoggingInterceptor<NotificationEvent> loggingInterceptor) {
    final var listenerContainerFactory = listenerContainerFactory(notificationConsumerFactory());
    listenerContainerFactory.setRecordInterceptor(loggingInterceptor);
    return listenerContainerFactory;
  }

  @Bean
  public NewTopic notificationEventTopic(NotificationTopicProperties notificationTopic) {
    return TopicBuilder.name(notificationTopic.getName())
      .partitions(notificationTopic.getPartitions())
      .replicas(notificationTopic.getReplicas())
      .build();
  }

  @Bean
  public NewTopic notificationEventRetryTopic(NotificationTopicProperties notificationTopic) {
    return TopicBuilder.name(notificationTopic.getRetryName())
      .partitions(notificationTopic.getPartitions())
      .replicas(notificationTopic.getReplicas())
      .build();
  }

}
