package com.tuum.testassignment.transaction;

import com.tuum.testassignment.common.event.TransactionCreated;
import com.tuum.testassignment.transaction.dto.TransactionCreatedResponse;
import com.tuum.testassignment.transaction.dto.TransactionRequest;
import com.tuum.testassignment.transaction.dto.TransactionResponse;
import com.tuum.testassignment.transaction.dto.TransactionsResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionDTOMapper {

    Transaction transactionRequestToTransaction(TransactionRequest transactionRequest);

    @Mapping(source = "transaction.id", target = "transactionId")
    TransactionResponse transactionToTransactionResponse(Transaction transaction);

    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "amount", target = "balance")
    TransactionCreatedResponse transactionToTransactionResponse(Transaction transaction, Long amount);

    TransactionCreated transactionToTransactionCreated(Transaction transaction);

    default TransactionsResponse transactionsToTransactionsResponse(List<Transaction> transactions) {
        return new TransactionsResponse(transactions.stream().map(this::transactionToTransactionResponse).toList());
    }
}
