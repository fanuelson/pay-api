package com.example.demo.infra.http.input.resources;

import java.util.List;
public record TransferListRequest(
  List<TransferRequest> transactions
) {
}
