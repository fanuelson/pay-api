package com.example.demo.domain.exception;

import com.example.demo.domain.transaction.validation.ValidationError;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.application.helper.StringHelper.join;

@Getter
public class BusinessValidationException extends BusinessException {

  private final List<ValidationError> errors;

  public BusinessValidationException(List<ValidationError> errors) {
    super(buildMessage(errors));
    this.errors = errors;
  }

  private static String buildMessage(List<ValidationError> errors) {
    return join(
      "Business validation failed with " + errors.size(),
      " error(s): ",
      errors.stream()
        .map(it -> join(it.code(), ": ", it.message()))
        .collect(Collectors.joining(", "))
    );
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
    return errors.stream().anyMatch(e -> e.code().equals(code));
  }

}