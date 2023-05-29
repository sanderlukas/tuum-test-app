package com.tuum.testassignment.transaction.dto;

import java.util.List;

public record TransactionsResponse(List<TransactionResponse> transactions) {
}
