package com.example.demo.domain.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationResult {

  private boolean isValid;
  private List<ValidationError> errors;

  public static ValidationResult valid() {
    return new ValidationResult(true, List.of());
  }

  public static ValidationResult invalid(String code, String message) {
    return new ValidationResult(false, List.of(ValidationError.of(code, message)));
  }

  public static ValidationResult invalid(String code, String message, String field) {
    return new ValidationResult(false, List.of(ValidationError.of(code, field, message)));
  }

  public static ValidationResult invalid(
    String code, String message, String field, Object value
  ) {
    return new ValidationResult(false, List.of(ValidationError.of(code, message, field, value)));
  }

  public static ValidationResult invalid(List<ValidationError> errors) {
    return new ValidationResult(false, errors);
  }

  public boolean isInvalid() {
    return !isValid;
  }
}