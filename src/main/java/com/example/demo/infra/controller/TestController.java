package com.example.demo.infra.controller;

import com.example.demo.domain.port.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final AuthorizationService authorizationService;

  @GetMapping("/authorize")
  public Map<String, Object> authorize() {
    final var res = authorizationService.authorize(1L, 1L, 100L);
    return Map.of(
      "authorized", res.isAuthorized(),
      "authorizationCode", res.getAuthorizationCode(),
      "msg", res.getMessage()
    );
  }

}
