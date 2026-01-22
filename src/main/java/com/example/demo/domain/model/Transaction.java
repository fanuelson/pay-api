package com.example.demo.domain.model;

import com.example.demo.domain.helper.DateTimeHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  private String id;
  private Long payerId;
  private Long payeeId;
  private Long amountInCents;
  private TransactionStatus status;
  private String authorizationCode;
  private String errorMessage;
  private LocalDateTime createdAt;
  private LocalDateTime completedAt;

  public Transaction(
    Long payerId,
    Long payeeId,
    Long amountInCents
  ) {
    this.id = UUID.randomUUID().toString();
    this.payerId = payerId;
    this.payeeId = payeeId;
    this.amountInCents = amountInCents;
    this.status = TransactionStatus.PENDING;
    this.createdAt = DateTimeHelper.now();

    validate();
  }

  private void validate() {
    if (payerId.equals(payeeId)) {
      throw new IllegalArgumentException("Pagador e recebedor não podem ser o mesmo usuário");
    }
    if (amountInCents <= 0) {
      throw new IllegalArgumentException("Valor da transação deve ser maior que zero");
    }
  }

  public void authorized(String code) {
    if (isNotPending()) {
      throw new IllegalStateException("Apenas transações PENDING podem ser autorizadas");
    }
    this.status = TransactionStatus.AUTHORIZED;
    this.authorizationCode = code;
  }

  public void completed() {
    if (isNotPending() && isNotAuthorized()) {
      throw new IllegalStateException("Apenas transações AUTHORIZED ou PENDING podem ser completadas");
    }
    this.status = TransactionStatus.COMPLETED;
    this.completedAt = DateTimeHelper.now();
  }

  public void failed(String reason) {
    this.status = TransactionStatus.FAILED;
    this.errorMessage = reason;
    this.completedAt = DateTimeHelper.now();
  }

  public void reverse(String reason) {
    if (isNotCompleted()) {
      throw new IllegalStateException("Apenas transações COMPLETED podem ser revertidas");
    }
    this.status = TransactionStatus.REVERSED;
    this.errorMessage = reason;
  }

  public boolean isNotAuthorized() {
    return status != TransactionStatus.AUTHORIZED;
  }

  public boolean isNotCompleted() {
    return status != TransactionStatus.COMPLETED;
  }

  public boolean isNotPending() {
    return status != TransactionStatus.PENDING;
  }
}