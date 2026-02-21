package com.example.demo.infra.messaging.notification;

import com.example.demo.infra.messaging.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class NotificationEventKafkaConfig extends KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public NewTopic notificationCreatedTopic() {
    return TopicBuilder.name("notification.created")
      .partitions(5)
      .build();
  }

  @Bean
  public NewTopic notificationCompletedTopic() {
    return TopicBuilder.name("notification.completed").build();
  }

  @Bean
  public NewTopic notificationFailedTopic() {
    return TopicBuilder.name("notification.failed").build();
  }


}
