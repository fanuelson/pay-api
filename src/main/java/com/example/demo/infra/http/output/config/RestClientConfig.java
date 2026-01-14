package com.example.demo.infra.http.output.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
      .requestInterceptor(loggingInterceptor())
      .build();
  }

  @Bean
  public RestClient authorizationRestClient() {
    return RestClient.builder()
      .baseUrl("https://util.devi.tools/api/v2")
      .requestInterceptor(loggingInterceptor())
      .build();
  }

  @Bean
  public RestClient notificationRestClient() {
    return RestClient.builder()
      .baseUrl("https://util.devi.tools/api/v1")
      .requestInterceptor(loggingInterceptor())
      .build();
  }

  private ClientHttpRequestInterceptor loggingInterceptor() {
    return (request, body, execution) -> {
      long start = System.currentTimeMillis();
      log.info("→ Request: {} {}", request.getMethod(), request.getURI());
      var response = execution.execute(request, body);
      long duration = System.currentTimeMillis() - start;
      log.info("← Response: {} ({}ms)", response.getStatusCode(), duration);
      return response;
    };
  }
}