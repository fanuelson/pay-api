package com.example.demo.domain.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {

  private String code;
  private String message;
  private String field;
  private Object value;

  public static ValidationError of(String code, String message, String field, Object value) {
    return new ValidationError(code, message, field, value);
  }

  public static ValidationError of(String code, String message) {
    return new ValidationError(code, message, null, null);
  }
}