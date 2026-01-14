package com.example.demo.infra.cache;

import com.example.demo.domain.helper.StringHelper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ConstantDelay;
import org.redisson.config.DelayStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static java.util.Optional.ofNullable;

@Configuration
public class RedissonConfig {

  @Value("${spring.data.redis.host:localhost}")
  private String redisHost;

  @Value("${spring.data.redis.port:6379}")
  private int redisPort;

  @Value("${spring.data.redis.password:}")
  private String redisPassword;

  @Bean
  public RedissonClient redissonClient() {
    String address = "redis://%s:%d".formatted(redisHost, redisPort);

    DelayStrategy retryDelay = new ConstantDelay(Duration.ofSeconds(2));

    Config config = new Config();
    config.useSingleServer()
      .setAddress(address)
      .setConnectionPoolSize(64)
      .setConnectionMinimumIdleSize(10)
      .setIdleConnectionTimeout(10000)
      .setTimeout(3000)
      .setRetryAttempts(3)
      .setRetryDelay(retryDelay);

    ofNullable(redisPassword)
      .filter(StringHelper::isNotBlank)
      .ifPresent(config::setPassword);

    return Redisson.create(config);
  }
}