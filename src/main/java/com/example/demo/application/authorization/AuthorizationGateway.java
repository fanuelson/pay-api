package com.example.demo.application.authorization;


public interface AuthorizationGateway {
  AuthorizationResult authorize(AuthorizationRequest request);
}