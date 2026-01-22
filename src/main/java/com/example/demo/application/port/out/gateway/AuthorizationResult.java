package com.example.demo.application.port.out.gateway;

public record AuthorizationResult(
  boolean isAuthorized,
  String authorizationCode,
  String message
) {

  public boolean isNotAuthorized() {
    return !isAuthorized;
  }

  public static AuthorizationResult authorized(String code) {
    return new AuthorizationResult(true, code, "Transação autorizada");
  }

  public static AuthorizationResult denied() {
    return new AuthorizationResult(false, null, "Authorization service denied the transfer");
  }

}