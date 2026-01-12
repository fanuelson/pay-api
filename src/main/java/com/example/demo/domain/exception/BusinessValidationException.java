package com.example.demo.domain.exception;

import com.example.demo.domain.validation.ValidationError;

import java.util.List;

public class BusinessValidationException extends DomainException {

  private final List<ValidationError> errors;

  public BusinessValidationException(List<ValidationError> errors) {
    super(buildMessage(errors));
    this.errors = errors;
  }

  public List<ValidationError> getErrors() {
    return errors;
  }

  private static String buildMessage(List<ValidationError> errors) {
    return "Business validation failed with " + errors.size() + " error(s): " +
      errors.stream()
        .map(e -> e.getCode() + ": " + e.getMessage())
        .reduce((a, b) -> a + ", " + b)
        .orElse("");
  }

  public static BusinessValidationException single(ValidationError error) {
    return new BusinessValidationException(List.of(error));
  }

  public static BusinessValidationException of(String code, String message, String field) {
    return new BusinessValidationException(
            List.of(new ValidationError(code, message, field, null))
    );
  }

  public boolean hasError(String code) {
    return errors.stream().anyMatch(e -> e.getCode().equals(code));
  }

  public List<ValidationError> getFieldErrors() {
    return errors.stream().filter(e -> e.getField() != null).toList();
  }

  public List<ValidationError> getGeneralErrors() {
    return errors.stream().filter(e -> e.getField() == null).toList();
  }
}