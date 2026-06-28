package com.example.demo.shared.domain.validation;


public interface Validator<C> {

  ValidationResult validate(C context);

  default boolean isValid(C context) {
    return validate(context).isValid();
  }

  default boolean isInvalid(C context) {
    return !isValid(context);
  }
}