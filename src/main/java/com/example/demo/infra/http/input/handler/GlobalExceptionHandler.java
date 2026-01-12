package com.example.demo.infra.http.input.handler;

import com.example.demo.domain.exception.BusinessValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BusinessValidationException.class)
  public ResponseEntity<Object> handleBusinessValidation(BusinessValidationException ex) {
    return ResponseEntity.badRequest().body(
      Map.of(
        "type", "BUSINESS_VALIDATION_ERROR",
        "errors", ex.getErrors()
      )
    );
  }
}