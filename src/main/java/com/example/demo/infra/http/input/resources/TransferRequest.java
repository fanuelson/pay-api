package com.example.demo.infra.http.input.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransferRequest(

  @NotNull
  Long payerId,

  @NotNull
  Long payeeId,

  @NotNull
  @Min(1)
  Long amountInCents

) {
}
