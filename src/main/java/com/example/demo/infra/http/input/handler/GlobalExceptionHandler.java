package com.example.demo.infra.http.input.handler;

import java.util.Map;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.domain.exception.BusinessValidationException;
import com.example.demo.domain.exception.DuplicateElementException;
import com.example.demo.domain.exception.ElementNotFoundException;
import com.example.demo.infra.exception.InfraException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    final var status = HttpStatus.INTERNAL_SERVER_ERROR;
    final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
    return ResponseEntity.status(status.value()).body(res);
  }

  @ExceptionHandler(InfraException.class)
  public ResponseEntity<ErrorResponse> handleException(InfraException ex) {
    final var status = HttpStatus.INTERNAL_SERVER_ERROR;
    final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
    return ResponseEntity.status(status.value()).body(res);
  }

  @ExceptionHandler(ElementNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleElementNotFound(ElementNotFoundException ex) {
    final var status = HttpStatus.NOT_FOUND;
    final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
    return ResponseEntity.status(status.value()).body(res);
  }

  @ExceptionHandler(DuplicateElementException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateElement(DuplicateElementException ex) {
    final var status = HttpStatus.CONFLICT;
    final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
    return ResponseEntity.status(status.value()).body(res);
  }

  @ExceptionHandler(BusinessValidationException.class)
  public ResponseEntity<Object> handleBusinessValidation(BusinessValidationException ex) {
    final var status = HttpStatus.BAD_REQUEST;
    final var res = ErrorResponse.of(status.toString(), "BUSINESS_VALIDATION_ERROR", ex.getMessage())
      .withErrors(ex.getErrors());
    return ResponseEntity.status(status.value()).body(res);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
    final var errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(formatMessage())
      .toList();
    return ResponseEntity.badRequest().body(Map.of("errors", errors));
  }

  private Function<FieldError, String> formatMessage() {
    return field -> String.join(" ", field.getField(), field.getDefaultMessage());
  }
}