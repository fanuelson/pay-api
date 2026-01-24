package com.example.demo.infra.messaging.transaction;

import com.example.demo.infra.messaging.KafkaTopicProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics.transaction-events")
public class TransactionTopicProperties extends KafkaTopicProperties {

}
