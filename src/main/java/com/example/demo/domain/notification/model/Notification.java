package com.example.demo.domain.notification.model;

import com.example.demo.application.helper.DateTimeHelper;
import lombok.*;
import java.time.LocalDateTime;
import java.util.function.Function;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  private NotificationId id;
  private String recipientAddress;
  private NotificationChannel channel;
  private String message;
  private LocalDateTime createdAt;

  public static Notification pending(
    String recipientAddress,
    NotificationChannel channel,
    String msg
  ) {
    return Notification.builder()
      .recipientAddress(recipientAddress)
      .channel(channel)
      .message(msg)
      .createdAt(DateTimeHelper.now())
      .build();
  }

  public static Function<NotificationChannel, Notification> fromChannel(String recipientAddress, String msg) {
    return channel -> pending(recipientAddress, channel, msg);
  }

}
