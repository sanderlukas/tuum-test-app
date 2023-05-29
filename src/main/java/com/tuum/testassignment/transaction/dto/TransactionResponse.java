package com.tuum.testassignment.transaction.dto;

public record TransactionResponse(Long transactionId,
                                  Long accountId,
                                  Long amount,
                                  String currency,
                                  String direction,
                                  String description) {
}
