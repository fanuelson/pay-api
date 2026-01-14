package com.example.demo.infra.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class RedisContainerConfig {

  @Container
  public static final GenericContainer<?> REDIS_CONTAINER =
    new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
      .withExposedPorts(6379)
      .withReuse(true);

  static {
    REDIS_CONTAINER.start();
  }

  @Bean
  @Primary
  public RedissonClient redissonClient() {
    Config config = new Config();
    String address = String.format(
      "redis://%s:%d",
      REDIS_CONTAINER.getHost(),
      REDIS_CONTAINER.getFirstMappedPort()
    );

    config.useSingleServer()
      .setAddress(address)
      .setConnectionPoolSize(10)
      .setConnectionMinimumIdleSize(2)
      .setTimeout(3000);

    return Redisson.create(config);
  }
}
