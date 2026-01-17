package com.example.demo.application.saga;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SagaOrchestrator<C> {

  private final List<SagaStep<C>> steps;

  public SagaOrchestrator(List<SagaStep<C>> steps) {
    this.steps = steps;
  }

  @SafeVarargs
  public static <X> SagaOrchestrator<X> of(SagaStep<X>... steps) {
    return new SagaOrchestrator<>(List.of(steps));
  }

  public void execute(C context) {
    List<SagaStep<C>> executedSteps = new ArrayList<>();

    for (SagaStep<C> step : steps) {
      try {
        log.info("Executing step: {}", step.getName());
        step.execute(context);
        executedSteps.add(step);
      } catch (Exception e) {
        log.error("Step failed: {}. Starting compensation.", step.getName(), e);
        compensate(executedSteps, context, e.getMessage());
        throw e;
      }
    }
  }

  private void compensate(List<SagaStep<C>> executedSteps, C context, String cause) {
    executedSteps.reversed().forEach(step -> {
      try {
        log.info("Compensating step: {}", step.getName());
        step.compensate(context, cause);
      } catch (Exception e) {
        log.error("Compensation failed for step: {}", step.getName(), e);
      }
    });
  }
}
