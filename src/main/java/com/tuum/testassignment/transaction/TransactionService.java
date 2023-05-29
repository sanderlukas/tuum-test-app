package com.tuum.testassignment.transaction;

import com.tuum.testassignment.account.AccountDAO;
import com.tuum.testassignment.balance.Balance;
import com.tuum.testassignment.common.event.BalanceUpdated;
import com.tuum.testassignment.exception.InsufficientFundsException;
import com.tuum.testassignment.exception.InvalidCurrencyException;
import com.tuum.testassignment.exception.ResourceNotFoundException;
import com.tuum.testassignment.rabbitmq.RabbitMQMessageProducer;
import com.tuum.testassignment.transaction.dto.TransactionCreatedResponse;
import com.tuum.testassignment.transaction.dto.TransactionRequest;
import com.tuum.testassignment.transaction.dto.TransactionsResponse;

import org.springframework.stereotype.Service;

import static com.tuum.testassignment.common.TransactionDirection.OUT;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final AccountDAO accountDAO;
    private final TransactionDTOMapper transactionMapper;
    private final RabbitMQMessageProducer producer;

    public TransactionService(TransactionDAO transactionDAO, AccountDAO accountDAO, TransactionDTOMapper transactionMapper, RabbitMQMessageProducer producer) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
        this.transactionMapper = transactionMapper;
        this.producer = producer;
    }

    public TransactionsResponse getTransactions(Long accountId) {
        return transactionMapper.transactionsToTransactionsResponse(transactionDAO.getTransactionsByAccountId(accountId));
    }

    public TransactionCreatedResponse createTransaction(TransactionRequest request) {
        if (!accountDAO.existsAccountWithAccountId(request.accountId())) {
            throw new ResourceNotFoundException("Account with id [%s] not found", request.accountId());
        }

        Balance selectedBalance = accountDAO.getBalances(request.accountId()).stream()
                .filter(balance -> balance.getCurrency().equals(request.currency().name()))
                .findFirst()
                .orElseThrow(() -> new InvalidCurrencyException("Account [%s] does not support transactions in %s", request.accountId(), request.currency()));

        long requestedAmount = Long.parseLong(request.amount());

        if (request.direction() == OUT && requestedAmount > selectedBalance.getAmount()) {
            throw new InsufficientFundsException("Balance [%s] does not have enough funds", selectedBalance.getId());
        }

        switch (request.direction()) {
            case IN -> selectedBalance.add(requestedAmount);
            case OUT -> selectedBalance.deduct(requestedAmount);
        }

        Transaction transaction = transactionMapper.transactionRequestToTransaction(request);
        transactionDAO.createTransaction(transaction);
        producer.publish(transactionMapper.transactionToTransactionCreated(transaction));

        accountDAO.updateBalance(selectedBalance);
        producer.publish(new BalanceUpdated(selectedBalance.getId(), selectedBalance.getAccountId(), selectedBalance.getCurrency(), selectedBalance.getAmount()));

        return transactionMapper.transactionToTransactionResponse(transaction, selectedBalance.getAmount());
    }
}
