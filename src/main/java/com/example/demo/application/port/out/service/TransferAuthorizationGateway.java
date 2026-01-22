package com.example.demo.application.port.out.service;


public interface TransferAuthorizationGateway {
  AuthorizationResult authorize(AuthorizationRequest request);
}