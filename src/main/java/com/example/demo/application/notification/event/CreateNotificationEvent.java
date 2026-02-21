package com.example.demo.application.notification.event;

import com.example.demo.domain.user.model.UserId;
import lombok.Getter;

@Getter
public class CreateNotificationEvent extends NotificationEvent {

  private final UserId userId;
  private final String message;

  protected CreateNotificationEvent(final UserId userId, final String message) {
    super(null);
    this.userId = userId;
    this.message = message;
  }

  public static CreateNotificationEvent of(final UserId userId, final String message) {
    return new CreateNotificationEvent(userId, message);
  }
}
