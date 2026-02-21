package com.example.demo.infra.repository.transaction.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {
}
