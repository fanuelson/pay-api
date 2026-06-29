package com.example.demo.wallet.adapter.in.web.handler;

import com.example.demo.shared.adapter.in.web.response.ErrorResponse;
import com.example.demo.wallet.domain.exception.InsufficientBalanceException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class WalletExceptionHandler {

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
    final var status = HttpStatus.UNPROCESSABLE_CONTENT;
    final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
    return ResponseEntity.status(status.value()).body(res);
  }
}
