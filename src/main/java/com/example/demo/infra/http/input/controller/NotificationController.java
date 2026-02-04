package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationChannel;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.infra.http.input.resources.SendNotificationRequest;
import com.example.demo.infra.http.input.resources.TransferRequest;
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
  private final TransactionRepository transactionRepository;
  private final CreateNotificationUseCase createNotificationUseCase;

  @PostMapping("/send")
  public Object sendNotification(@RequestBody SendNotificationRequest request) {
    final var sent = notificationService.send(
      Notification.pending(
        request.recipientId(),
        request.recipientAddress(),
        NotificationChannel.EMAIL,
        request.message()
      )
    );
    return Map.of("sent", sent);
  }

  @PostMapping("create")
  public String notifyEvent(@RequestBody TransferRequest request) {
    final var recipientId = request.payerId();
    createNotificationUseCase.execute(CreateNotificationCommand.of(recipientId, "heeey"));
    return "ok";
  }

}
