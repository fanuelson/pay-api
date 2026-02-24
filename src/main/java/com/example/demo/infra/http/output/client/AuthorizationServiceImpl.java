package com.example.demo.infra.http.output.client;

import com.example.demo.application.port.out.service.AuthorizationResponse;
import com.example.demo.application.port.out.service.AuthorizationService;
import com.example.demo.infra.http.output.exception.AuthorizationException;
import com.example.demo.infra.http.output.resource.AuthorizationResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.UUID;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

  @Autowired
  @Qualifier("authorizationRestClient")
  private RestClient restClient;

  @Override
  @CircuitBreaker(name = "authorizationService", fallbackMethod = "authorizationFallback")
  public AuthorizationResponse authorize(Long payerId, Long payeeId, Long amountInCents) {

    final var response = restClient.get()
      .uri("/authorize")
      .retrieve()
      .onStatus(
        HttpStatusCode::isError,
        (req, res) -> {
          throw AuthorizationException.of(res.getStatusText());
        }
      )
      .body(AuthorizationResponseDTO.class);

    if (isNull(response) || response.isUnauthorized()) {
      return AuthorizationResponse.denied();
    }

    return AuthorizationResponse.authorized(generateAuthorizationCode());

  }

  private AuthorizationResponse authorizationFallback(
    Long payerId, Long payeeId, Long amountInCents, Throwable ex
  ) {
    log.error("Circuit breaker activated for authorization service", ex);
    throw AuthorizationException.of(ex);
  }

  private String generateAuthorizationCode() {
    return "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

}
