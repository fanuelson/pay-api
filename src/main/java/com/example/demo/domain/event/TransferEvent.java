package com.example.demo.domain.event;

import com.example.demo.domain.vo.TransactionId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.util.function.Consumer;

@Getter
@ToString
@RequiredArgsConstructor
public class TransferEvent {

  private final TransactionEventKey key;
  private final TransactionEventType type;

  private final TransactionId transactionId;
  private final String authorizationCode;
  private final String cause;

  public static TransferEvent of(
    TransactionEventKey key,
    TransactionEventType type,
    TransactionId transactionId,
    String authorizationCode,
    String cause
  ) {
    return new TransferEvent(key, type, transactionId, authorizationCode, cause);
  }

  public static TransferEvent of(
    TransactionEventKey key,
    TransactionEventType type,
    TransactionId transactionId
  ) {
    return of(key, type, transactionId, null, null);
  }

  public TransferEvent to(TransactionEventType type) {
    return of(key, type, transactionId, authorizationCode, cause);
  }

  public TransferEvent withAuthorizationCode(String authorizationCode) {
    return of(key, type, transactionId, authorizationCode, cause);
  }

  public TransferEvent withCause(String cause) {
    return of(key, type, transactionId, authorizationCode, cause);
  }

  public void publish(Consumer<TransferEvent> publisher) {
    publisher.accept(this);
  }
}
