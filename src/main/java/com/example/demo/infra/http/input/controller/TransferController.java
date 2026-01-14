package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateTransactionCommand;
import com.example.demo.application.usecase.CreateTransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

  private final CreateTransactionUseCase createTransactionUseCase;

  @GetMapping
  public void create() {
    createTransactionUseCase.execute(CreateTransactionCommand.of(1L, 2L, 150L));
  }
}
