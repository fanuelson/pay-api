package com.example.demo.application.authorization;

import com.example.demo.domain.transaction.model.TransactionId;
public record AuthorizationRequest(
  TransactionId transactionId,
  Long payerId,
  Long payeeId,
  Long amountInCents
) {


}
