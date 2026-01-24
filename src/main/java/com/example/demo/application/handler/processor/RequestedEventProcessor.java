package com.example.demo.application.handler.processor;

import com.example.demo.domain.event.TransactionEventType;
import com.example.demo.domain.event.TransferEvent;
import com.example.demo.domain.exception.BusinessValidationException;
import com.example.demo.domain.validation.TransferContext;
import com.example.demo.domain.validation.TransferValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestedEventProcessor implements TransferEventProcessor {

  private final List<TransferValidator> validators;

  @Override
  public TransactionEventType getEventType() {
    return TransactionEventType.REQUESTED;
  }

  @Override
  public Optional<TransferEvent> process(TransferProcessorContext context) {
    final var transferContext = new TransferContext(
      context.getPayerId(),
      context.getPayeeId(),
      context.getAmountInCents(),
      context.getPayer(),
      context.getPayee(),
      context.getPayerWallet(),
      context.getPayeeWallet()
    );

    validators.forEach(validator -> {
      var result = validator.validate(transferContext);
      if (result.isInvalid()) {
        throw new BusinessValidationException(result.getErrors());
      }
    });

    return Optional.of(context.event().to(TransactionEventType.VALIDATED));
  }
}
