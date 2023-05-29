package com.tuum.testassignment.transaction;

import jakarta.validation.Valid;

import com.tuum.testassignment.transaction.dto.TransactionCreatedResponse;
import com.tuum.testassignment.transaction.dto.TransactionRequest;
import com.tuum.testassignment.transaction.dto.TransactionsResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionsResponse getTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactions(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionCreatedResponse createTransaction(@RequestBody @Valid TransactionRequest request) {
        return transactionService.createTransaction(request);
    }

}
