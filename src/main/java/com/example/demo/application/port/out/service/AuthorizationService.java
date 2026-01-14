package com.example.demo.application.port.out.service;


public interface AuthorizationService {
  AuthorizationResponse authorize(Long payerId, Long payeeId, Long amountInCents);
}