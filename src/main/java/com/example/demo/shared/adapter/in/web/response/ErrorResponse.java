package com.example.demo.shared.adapter.in.web.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.util.List;

@With
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String status;
    private final String error;
    private final String message;
    private final List<? extends Object> errors;

    public static ErrorResponse of(final String status, final String error, final String message) {
        return new ErrorResponse(status, error, message, List.of());
    }
}
