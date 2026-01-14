package com.example.demo.infra.http.output.client;

import com.example.demo.infra.exception.InfraException;
import com.example.demo.domain.port.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
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

  @CircuitBreaker(name = "notificationService", fallbackMethod = "notificationFallback")
  @Retry(name = "notificationService")
  public void sendNotification(Long userId, String email, String message) {
    log.info("Sending notification to user: {}, email: {}", userId, email);

    try {
      final var response = restClient.post()
        .uri("/notify")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Map.of("msg", "hi"))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
          log.error("Client error: {}", res.getStatusCode());
          throw new InfraException("Client error: " + res.getStatusCode());
        })
        .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
          log.error("Server error: {}", res.getStatusCode());
          throw new InfraException("Server error: " + res.getStatusCode());
        })
        .toBodilessEntity();

      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("Notification sent successfully to: {}", email);
      } else {
        log.warn("Notification service returned non-success status for: {}", email);
        throw new InfraException("Notification error, status: " + response.getStatusCode());
      }
    } catch (Exception ex) {
      log.error("Error sending notification", ex);
      throw new InfraException("Error: " + ex.getMessage());
    }
  }

  private void notificationFallback(Long userId, String email, String message, Exception ex) {
    log.error("Circuit breaker activated for notification service", ex);
    log.warn("Notification for user {} will be retried later", userId);
  }

}
