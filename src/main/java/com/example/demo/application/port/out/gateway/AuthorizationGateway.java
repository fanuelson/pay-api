package com.example.demo.application.port.out.gateway;


public interface AuthorizationGateway {
  AuthorizationResult authorize(AuthorizationRequest request);
}