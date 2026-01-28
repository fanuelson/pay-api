package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.port.out.gateway.NotificationGateway;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import com.example.demo.domain.model.Notification;
import com.example.demo.domain.model.NotificationChannel;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.domain.vo.TransactionId;
import com.example.demo.domain.vo.UserId;
import com.example.demo.infra.http.input.resources.SendNotificationRequest;
import com.example.demo.infra.http.input.resources.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
        TransactionId.of(request.transactionId()),
        request.recipientId(),
        request.recipientAddress(),
        NotificationChannel.EMAIL,
        request.message()
      )
    );
    return Map.of("sent", sent);
  }

  @PostMapping("create/{transactionId}")
  public String notifyEvent(
    @PathVariable String transactionId,
    @RequestBody TransferRequest request) {
    final var t = transactionRepository.save(
      Transaction.builder()
        .id(TransactionId.of(transactionId))
        .payerId(UserId.of(request.payerId()))
        .payeeId(UserId.of(request.payeeId()))
        .amountInCents(request.amountInCents())
        .status(TransactionStatus.PENDING)
        .build()
    );
    final var recipientId = request.payerId();
    createNotificationUseCase.execute(CreateNotificationCommand.of(t.getId(), recipientId, "heeey"));
    return "ok";
  }

}
