package com.example.demo.domain.validation;


public interface Validator<C> {

  ValidationResult validate(C context);

  default boolean isValid(C context) {
    return validate(context).isValid();
  }

  default boolean isNotValid(C context) {
    return !isValid(context);
  }
}