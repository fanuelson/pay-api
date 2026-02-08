package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.domain.notification.model.Notification;
import com.example.demo.domain.notification.model.NotificationChannel;
import com.example.demo.infra.http.input.resources.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationGateway notificationService;

  @PostMapping("/send")
  public Object sendNotification(@RequestBody SendNotificationRequest request) {
    final var sent = notificationService.send(
      Notification.pending(
        request.recipientAddress(),
        NotificationChannel.EMAIL,
        request.message()
      )
    );
    return Map.of("sent", sent);
  }

}
