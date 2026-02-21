package com.example.demo.infra.http.input.resources;

public record TransferRequest(
  Long payer,
  Long payee,
  Long amount
) {
}
