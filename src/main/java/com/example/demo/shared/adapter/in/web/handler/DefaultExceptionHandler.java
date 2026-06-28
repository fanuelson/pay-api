package com.example.demo.shared.adapter.in.web.handler;

import com.example.demo.shared.adapter.in.web.response.ErrorResponse;
import com.example.demo.shared.domain.exception.BusinessValidationException;
import com.example.demo.shared.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.function.Function;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        final var status = HttpStatus.INTERNAL_SERVER_ERROR;
        final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(status.value()).body(res);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        final var status = HttpStatus.NOT_FOUND;
        final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(status.value()).body(res);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidation(BusinessValidationException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), "Business validation failed")
                .withErrors(ex.getErrors());
        return ResponseEntity.status(status.value()).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        final List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(formatMessage())
                .toList();
        final var res = ErrorResponse.of(status.toString(), ex.getClass().getName(), "Validation failed")
                .withErrors(errors);
        return ResponseEntity.status(status.value()).body(res);
    }

    private Function<FieldError, String> formatMessage() {
        return field -> String.join(" ", field.getField(), field.getDefaultMessage());
    }
}
