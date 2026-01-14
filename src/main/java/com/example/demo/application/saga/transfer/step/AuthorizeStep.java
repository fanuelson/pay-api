package com.example.demo.application.saga.transfer.step;

import com.example.demo.application.saga.SagaStep;
import com.example.demo.application.saga.transfer.TransferSagaContext;
import com.example.demo.application.port.out.service.AuthorizationService;
import com.example.demo.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizeStep implements SagaStep<TransferSagaContext> {

  private final AuthorizationService authorizationService;

  @Override
  public String getName() {
    return "Authorize";
  }

  @Override
  public void execute(TransferSagaContext context) {
    var response = authorizationService.authorize(
        context.getPayerId(),
        context.getPayeeId(),
        context.getAmountInCents()
    );

    log.info("Authorization response: authorized={}, message={}",
        response.isAuthorized(), response.getMessage());

    if (!response.isAuthorized()) {
      throw new BusinessException(response.getMessage());
    }

    context.getTransaction().authorized(response.getAuthorizationCode());
  }

  @Override
  public void compensate(TransferSagaContext context, Exception cause) {
  }
}
