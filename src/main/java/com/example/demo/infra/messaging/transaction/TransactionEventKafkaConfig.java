package com.example.demo.infra.messaging.transaction;

import com.example.demo.infra.messaging.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class TransactionEventKafkaConfig extends KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public NewTopic transactionCreatedTopic() {
    return TopicBuilder.name("transaction.created")
      .partitions(5)
      .build();
  }

  @Bean
  public NewTopic transactionCompletedTopic() {
    return TopicBuilder.name("transaction.completed")
      .partitions(1)
      .build();
  }

  @Bean
  public NewTopic transactionFailedTopic() {
    return TopicBuilder.name("transaction.failed")
      .partitions(1)
      .build();
  }

}
