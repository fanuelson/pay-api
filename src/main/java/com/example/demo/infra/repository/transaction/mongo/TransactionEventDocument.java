package com.example.demo.infra.repository.transaction.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDocument {

  private String type;
  private Map<String, Object> payload;
  private String status;
  private String failedReason;
}
