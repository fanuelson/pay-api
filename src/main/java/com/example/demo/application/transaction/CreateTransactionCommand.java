package com.example.demo.application.transaction;

public record CreateTransactionCommand(
  Long payer,
  Long payee,
  Long amount
) {
}
