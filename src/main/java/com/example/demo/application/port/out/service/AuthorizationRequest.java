package com.example.demo.application.port.out.service;

import com.example.demo.domain.vo.TransactionId;
public record AuthorizationRequest(
  TransactionId transactionId,
  Long payerId,
  Long payeeId,
  Long amountInCents
) {


}
