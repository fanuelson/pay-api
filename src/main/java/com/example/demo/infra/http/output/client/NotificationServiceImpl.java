package com.example.demo.infra.http.output.client;

import com.example.demo.domain.port.service.NotificationService;
import com.example.demo.infra.exception.InfraException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  @Qualifier("notificationRestClient")
  private RestClient restClient;

  @Override
  @Retry(name = "notificationService")
  @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
  public boolean sendNotification(Long userId, String email, String message) {
    log.info("Sending notification to userId={}, email={}", userId, email);

//    throw new InfraException("AAA Notification failed: ");
    var response = restClient.post()
      .uri("/notify")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of("message", message))
      .retrieve()
      .toBodilessEntity();

    if (response.getStatusCode().is2xxSuccessful()) {
      log.info("Notification sent successfully to {}", email);
      return true;
    }

    throw new InfraException("Notification failed: " + response.getStatusCode());
  }

  private boolean fallback(Long userId, String email, String message, Exception ex) {
    log.warn("Circuit breaker open - userId={}, reason={}", userId, ex.getMessage());
    return false;
  }
}
