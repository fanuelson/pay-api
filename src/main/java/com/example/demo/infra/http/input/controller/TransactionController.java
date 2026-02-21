package com.example.demo.infra.http.input.controller;

import com.example.demo.application.transaction.CreateTransactionCommand;
import com.example.demo.application.transaction.CreateTransactionUseCase;
import com.example.demo.infra.http.input.resources.TransferListRequest;
import com.example.demo.infra.http.input.resources.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final CreateTransactionUseCase createTransactionUseCase;

  @PostMapping("/transfer")
  @ResponseStatus(HttpStatus.CREATED)
  public Object transfer(@RequestBody TransferRequest request) {
    final var command = new CreateTransactionCommand(request.payer(), request.payee(), request.amount());
    final var transactionId = createTransactionUseCase.execute(command);
    return Map.of("transactionId", transactionId.value());
  }

  @PostMapping("/transfer/list")
  @ResponseStatus(HttpStatus.CREATED)
  public Object transferList(@RequestBody TransferListRequest request) {

    return request.transactions().stream()
      .map(it -> new CreateTransactionCommand(it.payer(), it.payee(), it.amount()))
      .map(createTransactionUseCase::execute)
      .toList();
  }

}
