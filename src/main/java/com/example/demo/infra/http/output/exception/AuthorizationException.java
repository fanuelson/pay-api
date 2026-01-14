package com.example.demo.infra.http.output.exception;

import com.example.demo.infra.exception.InfraException;

public class AuthorizationException extends InfraException {

  private AuthorizationException(final String msg, final Throwable t) {
    super(msg, t);
  }

  public static AuthorizationException of(Throwable t) {
    return new AuthorizationException("Authorization failed: " + t.getMessage(), t);
  }
}
