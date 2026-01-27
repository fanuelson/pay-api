package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFailedEventProcessor implements TransferEventProcessor {

  private final WalletRepository walletRepository;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.AUTHORIZATION_FAILED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    log.info("Compensating balance for transactionId={}, cause={}",
      context.getTransactionId(), context.getCause());

    walletRepository.credit(
      context.getPayerWallet(),
      context.getAmountInCents()
    );

    return Optional.of(context.event().to(TransactionEventType.FAILED));
  }
}
