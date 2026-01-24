package com.example.demo.infra.messaging.transfer;

import com.example.demo.infra.messaging.KafkaTopicProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics.transfer-events")
public class TransferTopicProperties extends KafkaTopicProperties {

}
