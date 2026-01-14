package com.example.demo.infra.http.output.client;

import com.example.demo.application.port.out.service.AuthorizationResponse;
import com.example.demo.application.port.out.service.AuthorizationService;
import com.example.demo.infra.http.output.exception.AuthorizationException;
import com.example.demo.infra.http.output.resource.AuthorizationResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

  @Autowired
  @Qualifier("authorizationRestClient")
  private RestClient restClient;

  @Override
  @CircuitBreaker(name = "authorizationService", fallbackMethod = "authorizationFallback")
  @Retry(name = "authorizationService")
  public AuthorizationResponse authorize(Long payerId, Long payeeId, Long amountInCents) {
    try {
      final var response = restClient.get()
        .uri("/authorize")
        .retrieve()
        .body(AuthorizationResponseDTO.class);

      if (nonNull(response) && response.isAuthorized()) {
        return AuthorizationResponse.authorized(generateAuthorizationCode());
      }
      return AuthorizationResponse.denied();
    } catch (Exception ex) {
      log.error("Error authorizing transfer", ex);
      throw AuthorizationException.of(ex);
    }
  }

  private AuthorizationResponse authorizationFallback(
    Long payerId, Long payeeId, Long amountInCents, Exception ex
  ) {
    log.error("Circuit breaker activated for authorization service", ex);
    return AuthorizationResponse.unavailable();
  }

  private String generateAuthorizationCode() {
    return "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

}
