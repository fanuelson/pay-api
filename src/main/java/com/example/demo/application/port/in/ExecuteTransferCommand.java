package com.example.demo.application.port.in;

import lombok.Value;

@Value
public class ExecuteTransferCommand {
  String transactionId;
}
