package com.example.demo.application.chain;

import com.example.demo.application.chain.transfer.TransferHandler;
import com.example.demo.domain.model.TransactionAggregate;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class TransferChain {

  private final List<TransferHandler> handlers;

  private TransferChain(List<TransferHandler> handlers) {
    this.handlers = List.copyOf(handlers);
  }

  public static Builder start(TransferHandler first) {
    return new Builder(first);
  }

  public static Builder start() {
    return new Builder();
  }

  /**
   * Executa todos os handlers em sequência.
   * Em caso de falha, compensa os handlers já executados em ordem reversa.
   * Usado para execução local (síncrona).
   */
  public void execute(TransactionAggregate context) {
    var executed = new ArrayDeque<TransferHandler>();
    for (var handler : handlers) {
      try {
        log.info("Executing handler: {}", handler.name());
        handler.execute(context);
        executed.push(handler);
      } catch (Exception e) {
        log.error("Handler failed: {}. Starting compensation.", handler.name(), e);
        executed.forEach(h -> safeCompensate(h, context, e));
        throw e;
      }
    }
  }

  /**
   * Carrega os dados do contexto e executa apenas o handler informado.
   * O primeiro handler da chain (LoadData) é sempre executado antes.
   * Usado pelos Kafka listeners no fluxo distribuído.
   */
  public void executeStep(TransferHandler target, TransactionAggregate context) {
    log.info("Executing handler: {}", target.name());
    target.execute(context);
  }

  /**
   * Carrega os dados do contexto e compensa os handlers ANTERIORES ao target, em reverso.
   * O target em si NÃO é compensado — ele falhou e nunca completou com sucesso.
   * Usado pelos @DltHandler: cada listener só precisa passar seu próprio handler.
   */
  public void compensateFrom(TransferHandler target, TransactionAggregate context, Exception cause) {
    int idx = indexOf(target);
    for (int i = idx - 1; i >= 0; i--) {
      safeCompensate(handlers.get(i), context, cause);
    }
  }

  private int indexOf(TransferHandler target) {
    for (int i = 0; i < handlers.size(); i++) {
      if (handlers.get(i) == target) return i;
    }
    throw new IllegalArgumentException("Handler not registered in chain: " + target.name());
  }

  private void safeCompensate(TransferHandler handler, TransactionAggregate context, Exception cause) {
    try {
      log.info("Compensating handler: {}", handler.name());
      handler.compensate(context, cause);
    } catch (Exception e) {
      log.error("Compensation failed for handler: {}", handler.name(), e);
    }
  }

  public static final class Builder {

    private final List<TransferHandler> handlers = new ArrayList<>();

    private Builder(TransferHandler first) {
      handlers.add(first);
    }

    private Builder() {
    }

    public Builder then(TransferHandler next) {
      handlers.add(next);
      return this;
    }

    public TransferChain build() {
      return new TransferChain(handlers);
    }
  }
}
