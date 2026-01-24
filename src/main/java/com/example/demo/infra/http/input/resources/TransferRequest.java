package com.example.demo.infra.http.input.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransferRequest(

  @NotNull
  Long payer,

  @NotNull
  Long payee,

  @NotNull
  @Min(1)
  Long amount

) {

  public Long payerId() {
    return payer;
  }

  public Long payeeId() {
    return payee;
  }

  public Long amountInCents() {
    return amount;
//    return Long.valueOf(String.valueOf(amount() * 100));
  }
}
