package com.example.demo.infra.http.input.controller;

import com.example.demo.domain.port.service.AuthorizationService;
import com.example.demo.domain.port.service.NotificationService;
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
  private final NotificationService notificationService;

  @GetMapping("/authorize")
  public Map<String, Object> authorize() {
    final var res = authorizationService.authorize(1L, 1L, 100L);
    return Map.of(
      "authorized", res.isAuthorized(),
      "authorizationCode", res.getAuthorizationCode(),
      "msg", res.getMessage()
    );
  }

  @GetMapping("/notify")
  public String sendNotification() {
    notificationService.notify(1L, "mail.com", "heeey");
    return "ok";
  }

}
