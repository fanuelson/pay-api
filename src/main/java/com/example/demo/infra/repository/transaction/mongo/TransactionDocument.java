package com.example.demo.infra.repository.transaction.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class TransactionDocument {

  @Id
  private String id;
  private String payerId;
  private String payeeId;
  private Long amountInCents;
}
