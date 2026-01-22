package com.example.demo.application.port.out.gateway;

import com.example.demo.domain.vo.TransactionId;
public record AuthorizationRequest(
  TransactionId transactionId,
  Long payerId,
  Long payeeId,
  Long amountInCents
) {


}
