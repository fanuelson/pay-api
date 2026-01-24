package com.example.demo.infra.messaging;

import lombok.Data;
import java.util.Objects;

@Data
public abstract class KafkaTopicProperties {
  private String name = null;
  private String retryName = null;
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
