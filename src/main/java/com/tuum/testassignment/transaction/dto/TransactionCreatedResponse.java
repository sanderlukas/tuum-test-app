package com.tuum.testassignment.transaction.dto;

public record TransactionCreatedResponse(Long transactionId,
                                         Long accountId,
                                         Long amount,
                                         String currency,
                                         String direction,
                                         String description,
                                         Long balance) {
}
