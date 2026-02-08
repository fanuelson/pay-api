package com.example.demo.domain.transaction.validation;


import com.example.demo.domain.exception.BusinessValidationException;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ValidationPipeline<C> {

  private final List<Validator<C>> validators;
  private final boolean failOnFirstError;

  public static <X> ValidationPipeline<X> of(List<Validator<X>> validators, boolean failOnFirstError) {
    return new ValidationPipeline<>(validators, failOnFirstError);
  }

  public static <X> ValidationPipeline<X> of(List<Validator<X>> validators) {
    return of(validators, true);
  }

  public void validate(C context) {

    List<ValidationError> errors = new ArrayList<>();

    for (final var validator : validators) {
      final var result = validator.validate(context);
      if (result.isInvalid()) {
        if (failOnFirstError) {
          throw new BusinessValidationException(result.getErrors());
        }
        errors.addAll(result.getErrors());
      }
    }

    if (!errors.isEmpty()) {
      throw new BusinessValidationException(errors);
    }
  }
}
