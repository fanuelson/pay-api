package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.usecase.CreateTransactionUseCase;
import com.example.demo.infra.http.input.resources.ListTransferRequest;
import com.example.demo.infra.http.input.resources.TransferRequest;
import com.example.demo.infra.http.input.resources.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

  private final CreateTransactionUseCase createTransactionUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransferResponse create(@Valid @RequestBody TransferRequest request) {
    final var command = CreateTransactionCommand.of(
      request.payerId(),
      request.payeeId(),
      request.amountInCents()
    );
    final var output = createTransactionUseCase.execute(command);
    return new TransferResponse(output.transactionId().value());
  }

  @PostMapping("/list")
  @ResponseStatus(HttpStatus.CREATED)
  public List<TransferResponse> createList(@Valid @RequestBody ListTransferRequest request) {
    return request.transactions()
      .stream()
      .map(it ->
        CreateTransactionCommand.of(
          it.payerId(),
          it.payeeId(),
          it.amountInCents()
        )
      )
      .map(createTransactionUseCase::execute)
      .map(output -> new TransferResponse(output.transactionId().value()))
      .toList();
  }
}
