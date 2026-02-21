package com.example.demo.infra.http.input.controller;

import com.example.demo.application.authorization.AuthorizationGateway;
import com.example.demo.application.authorization.AuthorizationRequest;
import com.example.demo.application.notification.event.CreateNotificationEvent;
import com.example.demo.application.notification.CreateNotificationUseCase;
import com.example.demo.application.notification.NotificationGateway;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.model.NotificationChannel;
import com.example.demo.domain.transaction.model.TransactionId;
import com.example.demo.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import static java.util.Optional.ofNullable;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final AuthorizationGateway authorizationService;
  private final NotificationGateway notificationService;
  private final CreateNotificationUseCase notificationEngine;

  @GetMapping("/authorize")
  public Map<String, Object> authorize() {
    final var res = authorizationService.authorize(new AuthorizationRequest(TransactionId.generate(), 1L, 1L, 100L));
    return Map.of(
      "authorized", res.isAuthorized(),
      "authorizationCode", ofNullable(res.authorizationCode()).orElse(""),
      "msg", ofNullable(res.message()).orElse("")
    );
  }

  @GetMapping("/notify")
  public Object sendNotification() {

    final var sent = notificationService.send(
      Notification.pending("mail.com", NotificationChannel.EMAIL, "heeey")
    );
    return Map.of("sent", sent);
  }

  @PostMapping("notify-event")
  public String notifyEvent() {
    notificationEngine.handle(CreateNotificationEvent.of(UserId.of(5L), "heeey"));
    return "ok";
  }

}
