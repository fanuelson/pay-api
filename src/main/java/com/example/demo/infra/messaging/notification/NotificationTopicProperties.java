package com.example.demo.infra.messaging.notification;

import com.example.demo.infra.messaging.KafkaTopicProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics.notification-events")
public class NotificationTopicProperties extends KafkaTopicProperties {


}
