package com.tuum.testassignment.common.event;

public record TransactionCreated(Long transactionId,
                                 Long accountId,
                                 Long amount,
                                 String currency,
                                 String direction,
                                 String description) {
}
