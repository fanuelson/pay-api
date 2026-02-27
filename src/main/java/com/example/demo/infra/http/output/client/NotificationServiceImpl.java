package com.example.demo.infra.http.output.client;

import com.example.demo.application.exceptions.NotificationException;
import com.example.demo.application.port.out.service.NotificationService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

  @Override
  @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
  public boolean sendNotification(Long userId, String email, String message) {
    log.info("Sending notification to userId={}, email={}", userId, email);

    restClient.post()
      .uri("/notify")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of("message", message))
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        (req, res) -> {
          throw NotificationException.of(res.getStatusText());
        }
      )
      .toBodilessEntity();

    return true;
  }

  private boolean fallback(Long userId, String email, String message, CallNotPermittedException ex) {
    log.warn("Circuit breaker open - userId={}, reason={}", userId, ex.getMessage());
    throw ex;
  }

  private boolean fallback(Long userId, String email, String message, Throwable ex) {
    log.warn("Circuit breaker open2 - userId={}, reason={}", userId, ex.getMessage());
    throw NotificationException.of(ex.getMessage());
  }
}
