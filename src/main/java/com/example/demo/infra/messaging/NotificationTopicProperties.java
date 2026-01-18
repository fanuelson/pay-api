package com.example.demo.infra.messaging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics.notification-events")
public class NotificationTopicProperties {

  private String name;
  private String retryName;
  private int maxAttempts = 3;
  private int delay = 1000;
  private String dltName;
  private int partitions = 1;
  private short replicas = 1;

  public String getRetryName() {
    return Objects.isNull(retryName) ? name + "-retry" : retryName;
  }

  public String getDltName() {
    return Objects.isNull(dltName) ? name + "-dlt" : dltName;
  }
}
