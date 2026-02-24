package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.usecase.CreateTransactionUseCase;
import com.example.demo.infra.http.input.resources.TransferListRequest;
import com.example.demo.infra.http.input.resources.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransferController {

  private final CreateTransactionUseCase createTransactionUseCase;

  @GetMapping
  public void create() {
    createTransactionUseCase.execute(CreateTransactionCommand.of(1L, 2L, 150L));
  }

  @PostMapping("/transfer")
  @ResponseStatus(HttpStatus.CREATED)
  public Object transfer(@RequestBody TransferRequest request) {
    final var command = CreateTransactionCommand.of(request.payer(), request.payee(), request.amount());
    final var output = createTransactionUseCase.execute(command);
    return Map.of("transactionId", output.getTransactionId());
  }

  @PostMapping("/transfer/list")
  @ResponseStatus(HttpStatus.CREATED)
  public Object transferList(@RequestBody TransferListRequest request) {
    return request.transactions().stream()
      .map(it -> CreateTransactionCommand.of(it.payer(), it.payee(), it.amount()))
      .map(createTransactionUseCase::execute)
      .toList();
  }

}
