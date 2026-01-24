package com.example.demo.application.handler.processor;

import com.example.demo.application.port.out.gateway.AuthorizationGateway;
import com.example.demo.application.port.out.gateway.AuthorizationRequest;
import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationRequestedEventProcessor implements TransferEventProcessor {

  private final AuthorizationGateway authorizationGateway;
  private final TransactionRepository transactionRepository;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.AUTHORIZATION_REQUESTED;
  }

  @Override
  public TransactionEventType getErrorType() {
    return TransactionEventType.AUTHORIZATION_FAILED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    final var request = new AuthorizationRequest(
      context.getTransactionId(),
      context.getPayerId(),
      context.getPayeeId(),
      context.getAmountInCents()
    );

    final var response = authorizationGateway.authorize(request);

    log.info("Authorization response: authorized={}, message={}",
      response.isAuthorized(), response.message());

    if (response.isNotAuthorized()) {
      throw new BusinessException(response.message());
    }

    context.aggregate().authorize(response.authorizationCode());
    transactionRepository.save(context.getTransaction());

    return Optional.of(
      context.event()
        .to(TransactionEventType.AUTHORIZED)
        .withAuthorizationCode(response.authorizationCode())
    );
  }
}
