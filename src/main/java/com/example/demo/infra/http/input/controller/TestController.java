package com.example.demo.infra.http.input.controller;

import com.example.demo.application.port.in.CreateNotificationCommand;
import com.example.demo.application.usecase.CreateNotificationUseCase;
import com.example.demo.domain.model.Transaction;
import com.example.demo.domain.model.TransactionStatus;
import com.example.demo.application.port.out.event.NotificationEventPublisher;
import com.example.demo.domain.repository.TransactionRepository;
import com.example.demo.application.port.out.service.AuthorizationService;
import com.example.demo.application.port.out.service.LockService;
import com.example.demo.application.port.out.service.NotificationService;
import com.example.demo.infra.http.input.resources.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

  private final TransactionRepository transactionRepository;
  private final CreateNotificationUseCase createNotificationUseCase;
  private final NotificationEventPublisher notificationEventPublisher;

  @GetMapping("/authorize")
  public Map<String, Object> authorize() {
    final var res = authorizationService.authorize(1L, 1L, 100L);
    return Map.of(
      "authorized", res.isAuthorized(),
      "authorizationCode", ofNullable(res.authorizationCode()).orElse(""),
      "msg", ofNullable(res.message()).orElse("")
    );
  }

  @GetMapping("/notify")
  public Object sendNotification() {
    try {
      final var sent = notificationService.sendNotification(1L, "mail.com", "heeey");
      return Map.of("sent", sent);
    } catch (Exception e) {
      log.error("Error sending notification");
      return Map.of("sent", false);
    }


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

  @PostMapping("notify-event/{transactionId}")
  public String notifyEvent(
    @PathVariable String transactionId,
    @RequestBody TransferRequest request) {
    final var t = transactionRepository.save(
      Transaction.builder()
        .id(transactionId)
        .payerId(request.payerId())
        .payeeId(request.payeeId())
        .amountInCents(request.amountInCents())
        .status(TransactionStatus.PENDING)
        .build()
    );
    final var recipientId = request.payerId();
    createNotificationUseCase.execute(CreateNotificationCommand.of(t.getId(), recipientId, "heeey"));
    return "ok";
  }

}
