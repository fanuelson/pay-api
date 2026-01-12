package com.example.demo.infra.http.client;

import com.example.demo.domain.exception.AppException;
import com.example.demo.domain.model.AuthorizationResponse;
import com.example.demo.domain.port.service.AuthorizationService;
import com.example.demo.infra.http.resource.AuthorizationResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

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

      if(response != null && response.isAuthorized()) {
        return AuthorizationResponse.authorized(generateAuthorizationCode());
      }
      return AuthorizationResponse.denied("Authorization service denied the transfer");
    } catch (Exception ex) {
      log.error("Error authorizing transfer", ex);
      throw new AppException("Authorization failed: " + ex.getMessage());
    }
  }

  private AuthorizationResponse authorizationFallback(
    Long payerId, Long payeeId, Long amountInCents, Exception ex
  ) {
    log.error("Circuit breaker activated for authorization service", ex);
    return AuthorizationResponse.denied("Authorization service is temporarily unavailable");
  }

  private String generateAuthorizationCode() {
    return "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

}
