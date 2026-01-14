package com.example.demo.infra.http.input.controller;

import com.example.demo.domain.port.service.AuthorizationService;
import com.example.demo.domain.port.service.LockService;
import com.example.demo.domain.port.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final AuthorizationService authorizationService;
  private final NotificationService notificationService;
  private final LockService lockService;

  @GetMapping("/authorize")
  public Map<String, Object> authorize() {
    final var res = authorizationService.authorize(1L, 1L, 100L);
    return Map.of(
      "authorized", res.isAuthorized(),
      "authorizationCode", ofNullable(res.getAuthorizationCode()).orElse(""),
      "msg", ofNullable(res.getMessage()).orElse("")
    );
  }

  @GetMapping("/notify")
  public String sendNotification() {
    notificationService.sendNotification(1L, "mail.com", "heeey");
    return "ok";
  }

  @GetMapping("/lock/{key}")
  public String lock(@PathVariable final String key) {
    try {
      return lockService.withLock(key, 5, 30, TimeUnit.SECONDS, () -> {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        return "ok";
      });
    } catch (Exception e) {
      return e.getMessage();
    }
  }

}
