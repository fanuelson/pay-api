package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizedEventProcessor implements TransferEventProcessor {

  private final WalletRepository walletRepository;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.AUTHORIZED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    final var wallet = context.aggregate().creditPayee();
    walletRepository.update(wallet.getId(), wallet);

    return Optional.of(context.event().to(TransactionEventType.CREDITED));
  }
}
