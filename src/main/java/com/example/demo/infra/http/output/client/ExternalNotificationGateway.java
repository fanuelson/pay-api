package com.example.demo.infra.http.output.client;

import com.example.demo.application.exception.NotificationException;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.application.port.out.gateway.NotificationResult;
import com.example.demo.domain.notification.model.Notification;
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
public class ExternalNotificationGateway implements NotificationGateway {

  @Autowired
  @Qualifier("notificationRestClient")
  private RestClient restClient;

  @Override
  @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
  public NotificationResult send(Notification notification) {
    final var recipientAddress = notification.getRecipientAddress();
    log.info("Sending notification to recipientAddress={}", recipientAddress);

    var response = restClient.post()
      .uri("/notify")
      .contentType(MediaType.APPLICATION_JSON)
      .body(Map.of(
        "channel", notification.getChannel(),
        "receiver", recipientAddress,
        "message", notification.getMessage())
      )
      .retrieve()
      .toBodilessEntity();

    if (response.getStatusCode().isError()) {
      throw NotificationException.of(response.getStatusCode().toString());
    }

    log.info("Notification sent successfully to {}", recipientAddress);
    return NotificationResult.success();
  }

  private NotificationResult fallback(Notification notification, Exception ex) throws Exception {
    log.warn("Notification fallback called, reason = {}", ex.getMessage());
    throw NotificationException.of(ex);
  }

}
