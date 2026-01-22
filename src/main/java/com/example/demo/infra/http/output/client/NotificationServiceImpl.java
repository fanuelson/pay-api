package com.example.demo.infra.http.output.client;

import com.example.demo.application.exception.NotificationException;
import com.example.demo.application.port.out.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
  @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
  public boolean sendNotification(Long userId, String email, String message) {
    log.info("Sending notification to userId={}, email={}", userId, email);

    var response = restClient.post()
      .uri("/notify")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of("receiver", email, "message", message))
      .retrieve()
      .toBodilessEntity();

    if (response.getStatusCode().isError()) {
      throw NotificationException.create(response.getStatusCode().toString());
    }

    log.info("Notification sent successfully to {}", email);
    return true;
  }

  private boolean fallback(Long userId, String email, String message, Exception ex) throws Exception {
    log.warn("Fallback called, reason = {}", ex.getMessage());
    throw ex;
  }

}
