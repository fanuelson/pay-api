package com.example.demo.infra.http.output.client;

import static java.util.Objects.isNull;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.application.authorization.AuthorizationException;
import com.example.demo.application.authorization.AuthorizationGateway;
import com.example.demo.application.authorization.AuthorizationRequest;
import com.example.demo.application.authorization.AuthorizationResult;
import com.example.demo.infra.http.output.resource.AuthorizationResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalAuthorizationGateway implements AuthorizationGateway {

  @Autowired
  @Qualifier("authorizationRestClient")
  private RestClient restClient;

  @Override
  @CircuitBreaker(name = "authorizationService", fallbackMethod = "fallback")
  public AuthorizationResult authorize(AuthorizationRequest request) {
    final var response = restClient.get()
      .uri("/authorize")
      .retrieve()
      .body(AuthorizationResponseDTO.class);

    if (isNull(response) || response.isNotAuthorized()) {
      return AuthorizationResult.denied();
    }

    return AuthorizationResult.authorized(generateAuthorizationCode());
  }

  @SuppressWarnings("unused")
  private AuthorizationResult fallback(AuthorizationRequest request, Exception ex) {
    log.warn("Authorization fallback called, reason = {}", ex.getMessage());
    throw AuthorizationException.of(ex);
  }

  private String generateAuthorizationCode() {
    return "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

}
