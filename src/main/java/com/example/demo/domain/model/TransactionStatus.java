package com.example.demo.domain.model;

public enum TransactionStatus {
    PENDING,      // Transação criada, aguardando processamento
    AUTHORIZED,   // Autorizada pelo serviço externo
    COMPLETED,    // Concluída com sucesso
    FAILED,       // Falhou por algum motivo
    REVERSED      // Revertida/Estornada
}
