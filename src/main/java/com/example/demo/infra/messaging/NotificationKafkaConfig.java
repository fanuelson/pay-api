package com.example.demo.infra.messaging;

import com.example.demo.application.port.out.event.NotificationEvent;
import com.example.demo.infra.messaging.listener.NotificationEventRetryListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;


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
  public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> notificationListenerContainerFactory(
    NotificationEventRetryListener notificationEventRetryListener
  ) {
    final var listenerContainerFactory = listenerContainerFactory(notificationConsumerFactory());
    final var recoverer = deadLetterPublishingRecoverer(notificationKafkaTemplate());
    final var backOff = new FixedBackOff(10000, 5);
    final var errorHandler = new DefaultErrorHandler(recoverer, backOff);
    errorHandler.setRetryListeners(notificationEventRetryListener);
    listenerContainerFactory.setCommonErrorHandler(errorHandler);
    listenerContainerFactory.setConcurrency(2);

    return listenerContainerFactory;
  }

}
