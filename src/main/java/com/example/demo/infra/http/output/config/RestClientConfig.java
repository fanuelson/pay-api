package com.example.demo.infra.http.output.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

  @Value("${external.authorization.url}")
  private String authorizationUrl;

  @Value("${external.notification.url}")
  private String notificationUrl;

  @Bean
  public RestClient authorizationRestClient() {
    return RestClient.builder()
      .baseUrl(authorizationUrl)
      .requestInterceptor(loggingInterceptor())
      .build();
  }

  @Bean
  public RestClient notificationRestClient() {
    return RestClient.builder()
      .baseUrl(notificationUrl)
      .requestInterceptor(loggingInterceptor())
      .build();
  }

  private ClientHttpRequestInterceptor loggingInterceptor() {
    return (request, body, execution) -> {
      try {
        long start = System.currentTimeMillis();
        log.info("→ Request: {} {}", request.getMethod(), request.getURI());
        var response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - start;
        log.info("← Response: {} ({}ms)", response.getStatusCode(), duration);
        return response;
      } catch (Exception e) {
        log.error("RestClient exception ", e.getCause());
        throw e;
      }

    };
  }

}