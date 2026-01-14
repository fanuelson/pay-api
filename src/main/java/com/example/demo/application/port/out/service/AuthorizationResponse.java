package com.example.demo.application.port.out.service;

import lombok.Value;

@Value
public class AuthorizationResponse {

  boolean isAuthorized;
  String authorizationCode;
  String message;

  public static AuthorizationResponse authorized(String code) {
    return new AuthorizationResponse(true, code, "Transação autorizada");
  }

  public static AuthorizationResponse unavailable() {
    return new AuthorizationResponse(false, null, "Authorization service is temporarily unavailable");
  }

  public static AuthorizationResponse denied() {
    return new AuthorizationResponse(false, null, "Authorization service denied the transfer");
  }

}