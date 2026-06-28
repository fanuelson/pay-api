package com.example.demo.transaction.adapter.in.controller.request;

public record TransactionRequest(String amount, long payer, long payee) {
}
