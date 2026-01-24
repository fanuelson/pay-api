package com.example.demo.infra.http.input.resources;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ListTransferRequest(

  @NotNull
  List<TransferRequest> transactions

) {

}
