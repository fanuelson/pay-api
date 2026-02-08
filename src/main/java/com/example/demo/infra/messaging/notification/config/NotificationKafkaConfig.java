package com.example.demo.infra.messaging.notification.config;

import com.example.demo.domain.notification.model.NotificationEvent;
import com.example.demo.infra.messaging.KafkaConfig;
import com.example.demo.infra.messaging.KafkaTopicProperties;
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
public class NotificationKafkaConfig extends KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, NotificationEvent> notificationProducerFactory() {
    return producerFactory(kafkaProperties);
  }

  @Bean
  public KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate(
    KafkaProducerListener<NotificationEvent> producerListener
  ) {
    final var template = kafkaTemplate(notificationProducerFactory());
    template.setProducerListener(producerListener);
    return template;
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
  public NewTopic notificationRequestedTopic(NotificationTopicsProperties topics) {
    return buildTopic(topics.getNotificationRequested());
  }

  @Bean
  public NewTopic notificationSentTopic(NotificationTopicsProperties topics) {
    return buildTopic(topics.getNotificationSent());
  }

  private NewTopic buildTopic(KafkaTopicProperties props) {
    return TopicBuilder.name(props.getName())
      .partitions(props.getPartitions())
      .replicas(props.getReplicas())
      .build();
  }

}
