package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.port.out.gateway.AuthorizationRequest;
import com.example.demo.application.port.out.gateway.AuthorizationGateway;
import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizeStep implements SagaStep<TransferSagaContext> {

  private final AuthorizationGateway authorizationGateway;

  @Override
  public String getName() {
    return "Authorize";
  }

  @Override
  public void execute(TransferSagaContext context) {
    final var request = new AuthorizationRequest(
      context.getTransactionId(),
      context.getPayerId(),
      context.getPayeeId(),
      context.getAmountInCents()
    );

    var response = authorizationGateway.authorize(request);

    log.info("Authorization response: authorized={}, message={}",
      response.isAuthorized(), response.message());

    if (response.isNotAuthorized()) {
      throw new BusinessException(response.message());
    }

    context.getTransaction().authorized(response.authorizationCode());
  }

  @Override
  public void compensate(TransferSagaContext context, String cause) {
  }
}
