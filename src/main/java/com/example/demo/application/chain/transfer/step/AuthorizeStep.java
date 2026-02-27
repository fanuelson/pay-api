package com.example.demo.application.chain.transfer.step;

import com.example.demo.domain.model.TransactionAggregate;
import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.application.port.out.service.AuthorizationService;
import com.example.demo.domain.exception.BusinessException;
import com.example.demo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizeStep implements TransferHandler {

  private final AuthorizationService authorizationService;
  private final TransactionRepository transactionRepository;

  @Override
  public String name() {
    return "Authorize";
  }

  @Override
  public void execute(TransactionAggregate context) {
    var response = authorizationService.authorize(
        context.getPayerId(),
        context.getPayeeId(),
        context.getAmountInCents()
    );

    if (response.isUnauthorized()) {
      throw new BusinessException(response.getMessage());
    }

    context.getTransaction().authorized(response.getAuthorizationCode());
    transactionRepository.update(context.getTransactionId(), context.getTransaction());
  }
}
