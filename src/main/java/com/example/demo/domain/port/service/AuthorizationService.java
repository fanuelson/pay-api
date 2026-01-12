package com.example.demo.domain.port.service;


import com.example.demo.domain.model.AuthorizationResponse;

public interface AuthorizationService {
  AuthorizationResponse authorize(Long payerId, Long payeeId, Long amountInCents);
}