package com.example.demo.application.port.out.service;


public interface AuthorizationService {
  AuthorizationResult authorize(AuthorizationRequest request);
}