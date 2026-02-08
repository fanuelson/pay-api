package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.port.out.gateway.AuthorizationGateway;
import com.example.demo.application.port.out.gateway.AuthorizationRequest;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.model.NotificationChannel;
import com.example.demo.domain.transaction.model.TransactionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static java.util.Optional.ofNullable;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

  private final AuthorizationGateway authorizationService;
  private final NotificationGateway notificationService;
  private final CreateNotificationUseCase createNotificationUseCase;

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
    createNotificationUseCase.execute(CreateNotificationCommand.of(5L, "heeey"));
    return "ok";
  }

}
