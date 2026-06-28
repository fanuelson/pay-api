package com.example.demo.transaction.adapter.in.controller;

import com.example.demo.shared.domain.UserId;
import com.example.demo.transaction.adapter.in.controller.request.TransactionRequest;
import com.example.demo.transaction.application.port.in.CreateTransactionUseCase;
import com.example.demo.transaction.application.port.in.command.CreateTransactionCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

  private final CreateTransactionUseCase createTransaction;

  @PostMapping
  public ResponseEntity<String> transfer(@RequestBody TransactionRequest request) {
    final var amountInCents = new BigDecimal(request.amount())
        .movePointRight(2)
        .setScale(0, RoundingMode.HALF_EVEN)
        .longValueExact();
    final var command = new CreateTransactionCommand(
        amountInCents,
        UserId.from(request.payer()),
        UserId.from(request.payee())
    );
    final var id = createTransaction.execute(command);
    final var idValue = id.value();
    return ResponseEntity.created(URI.create("/v1/transaction/" + idValue))
        .body(idValue);
  }
}
