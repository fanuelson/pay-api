package com.example.demo.infra.messaging.notification.config;

import com.example.demo.infra.messaging.KafkaTopicProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics.notification-sent")
public class NotificationSentTopicProperties extends KafkaTopicProperties {
}
