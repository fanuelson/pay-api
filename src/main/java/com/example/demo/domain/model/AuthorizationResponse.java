package com.example.demo.domain.model;

import lombok.Value;

@Value
public class AuthorizationResponse {

  boolean isAuthorized;
  String authorizationCode;
  String message;

  public static AuthorizationResponse authorized(String code) {
    return new AuthorizationResponse(true, code, "Transação autorizada");
  }

  public static AuthorizationResponse denied(String reason) {
    return new AuthorizationResponse(false, null, reason);
  }

  public boolean isNotAuthorized() {
    return !isAuthorized;
  }
}