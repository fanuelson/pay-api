package com.example.demo.application.validation;

public record ValidationError(String code, String message, String field, Object value) {

  public static ValidationError of(String code, String message, String field, Object value) {
    return new ValidationError(code, message, field, value);
  }

  public static ValidationError of(String code, String message, String field) {
    return of(code, message, field, null);
  }

  public static ValidationError of(String code, String message) {
    return of(code, message, null, null);
  }
}