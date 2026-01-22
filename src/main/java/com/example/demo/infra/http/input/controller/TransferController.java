package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.usecase.CreateTransactionUseCase;
import com.example.demo.infra.http.input.resources.TransferRequest;
import com.example.demo.infra.http.input.resources.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

  private final CreateTransactionUseCase createTransactionUseCase;

  @PostMapping
  public TransferResponse create(@Valid @RequestBody TransferRequest transferRequest) {
    final var command = CreateTransactionCommand.of(
      transferRequest.payerId(),
      transferRequest.payeeId(),
      transferRequest.amountInCents()
    );
    final var output = createTransactionUseCase.execute(command);
    return new TransferResponse(output.transactionId().value());
  }
}
