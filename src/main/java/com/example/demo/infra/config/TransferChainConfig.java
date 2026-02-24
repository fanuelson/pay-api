package com.example.demo.infra.config;

import com.example.demo.application.chain.TransferChain;
import com.example.demo.application.chain.transfer.step.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferChainConfig {

  @Bean
  public TransferChain transferChain(
      LoadDataStep loadData,
      ValidateStep validate,
      ReserveBalanceStep reserveBalance,
      AuthorizeStep authorize,
      CreditStep credit,
      CompleteStep complete,
      NotifyStep notify
  ) {
    return TransferChain.start(loadData)
        .then(validate)
        .then(authorize)
        .then(reserveBalance)
        .then(credit)
        .then(complete)
        .then(notify)
        .build();
  }
}
