package com.tuum.testassignment.transaction;

import com.tuum.testassignment.account.AccountDAO;
import com.tuum.testassignment.balance.Balance;
import com.tuum.testassignment.common.CustomCurrency;
import com.tuum.testassignment.common.TransactionDirection;
import com.tuum.testassignment.exception.InsufficientFundsException;
import com.tuum.testassignment.exception.InvalidCurrencyException;
import com.tuum.testassignment.exception.ResourceNotFoundException;
import com.tuum.testassignment.rabbitmq.RabbitMQMessageProducer;
import com.tuum.testassignment.transaction.dto.TransactionCreatedResponse;
import com.tuum.testassignment.transaction.dto.TransactionRequest;
import com.tuum.testassignment.transaction.dto.TransactionsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {
        TransactionDTOMapperImpl.class
})
class TransactionServiceTest {

    @Autowired
    private TransactionDTOMapper transactionDTOMapper;

    @Mock
    private RabbitMQMessageProducer messageProducer;

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private AccountDAO accountDAO;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionDAO, accountDAO, transactionDTOMapper, messageProducer);
    }

    @Test
    void canGetTransactions() {
        Long accountId = 1L;
        List<Transaction> transactions = List.of(
                new Transaction(1L, accountId, 52500L, "USD", "IN", "Order-123"),
                new Transaction(2L, accountId, 10000L, "USD", "OUT", "Invest")
        );

        when(transactionDAO.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        TransactionsResponse expectedTransactions = transactionDTOMapper.transactionsToTransactionsResponse(transactions);
        TransactionsResponse actualTransactions = transactionService.getTransactions(accountId);
        assertThat(expectedTransactions).isEqualTo(actualTransactions);
    }

    @Test
    void createInTransaction() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, "123123", CustomCurrency.GBP, TransactionDirection.IN, "Order-3");
        when(accountDAO.existsAccountWithAccountId(transactionRequest.accountId())).thenReturn(true);

        Balance gbpBalance = new Balance(1L, transactionRequest.accountId(), 500000L, "GBP");
        when(accountDAO.getBalances(transactionRequest.accountId())).thenReturn(List.of(gbpBalance));

        TransactionCreatedResponse actualTransaction = transactionService.createTransaction(transactionRequest);

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        ArgumentCaptor<Balance> balanceArgumentCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(transactionDAO).createTransaction(transactionArgumentCaptor.capture());
        verify(accountDAO).updateBalance(balanceArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        Balance capturedBalance = balanceArgumentCaptor.getValue();

        // Assert transaction creation
        assertThat(capturedTransaction.getId()).isNull();
        assertThat(capturedTransaction.getAccountId()).isEqualTo(transactionRequest.accountId());
        assertThat(capturedTransaction.getAmount()).isEqualTo(Long.parseLong(transactionRequest.amount()));
        assertThat(capturedTransaction.getCurrency()).isEqualTo(transactionRequest.currency().name());
        assertThat(capturedTransaction.getDirection()).isEqualTo(transactionRequest.direction().name());
        assertThat(capturedTransaction.getDescription()).isEqualTo(transactionRequest.description());

        // Assert balance update
        assertThat(capturedBalance.getId()).isEqualTo(gbpBalance.getId());
        assertThat(capturedBalance.getAccountId()).isEqualTo(transactionRequest.accountId());
        assertThat(capturedBalance.getAmount()).isEqualTo(gbpBalance.getAmount());
        assertThat(capturedBalance.getCurrency()).isEqualTo(gbpBalance.getCurrency());

        // Assert response
        assertThat(actualTransaction.transactionId()).isNull();
        assertThat(actualTransaction.accountId()).isEqualTo(transactionRequest.accountId());
        assertThat(actualTransaction.amount()).isEqualTo(Long.parseLong(transactionRequest.amount()));
        assertThat(actualTransaction.currency()).isEqualTo(transactionRequest.currency().name());
        assertThat(actualTransaction.direction()).isEqualTo(transactionRequest.direction().name());
        assertThat(actualTransaction.description()).isEqualTo(transactionRequest.description());
        assertThat(actualTransaction.balance()).isEqualTo(gbpBalance.getAmount());
    }

    @Test
    void createOutTransaction() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, "123123", CustomCurrency.GBP, TransactionDirection.OUT, "Order-3");
        when(accountDAO.existsAccountWithAccountId(transactionRequest.accountId())).thenReturn(true);

        Balance gbpBalance = new Balance(1L, transactionRequest.accountId(), 500000L, "GBP");
        when(accountDAO.getBalances(transactionRequest.accountId())).thenReturn(List.of(gbpBalance));

        TransactionCreatedResponse actualTransaction = transactionService.createTransaction(transactionRequest);

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        ArgumentCaptor<Balance> balanceArgumentCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(transactionDAO).createTransaction(transactionArgumentCaptor.capture());
        verify(accountDAO).updateBalance(balanceArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        Balance capturedBalance = balanceArgumentCaptor.getValue();

        // Assert transaction creation
        assertThat(capturedTransaction.getId()).isNull();
        assertThat(capturedTransaction.getAccountId()).isEqualTo(transactionRequest.accountId());
        assertThat(capturedTransaction.getAmount()).isEqualTo(Long.parseLong(transactionRequest.amount()));
        assertThat(capturedTransaction.getCurrency()).isEqualTo(transactionRequest.currency().name());
        assertThat(capturedTransaction.getDirection()).isEqualTo(transactionRequest.direction().name());
        assertThat(capturedTransaction.getDescription()).isEqualTo(transactionRequest.description());

        // Assert balance update
        assertThat(capturedBalance.getId()).isEqualTo(gbpBalance.getId());
        assertThat(capturedBalance.getAccountId()).isEqualTo(transactionRequest.accountId());
        assertThat(capturedBalance.getAmount()).isEqualTo(gbpBalance.getAmount());
        assertThat(capturedBalance.getCurrency()).isEqualTo(gbpBalance.getCurrency());

        // Assert response
        assertThat(actualTransaction.transactionId()).isNull();
        assertThat(actualTransaction.accountId()).isEqualTo(transactionRequest.accountId());
        assertThat(actualTransaction.amount()).isEqualTo(Long.parseLong(transactionRequest.amount()));
        assertThat(actualTransaction.currency()).isEqualTo(transactionRequest.currency().name());
        assertThat(actualTransaction.direction()).isEqualTo(transactionRequest.direction().name());
        assertThat(actualTransaction.description()).isEqualTo(transactionRequest.description());
        assertThat(actualTransaction.balance()).isEqualTo(gbpBalance.getAmount());
    }

    @Test
    void createTransactionWithNonExistentAccount() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, "123123", CustomCurrency.GBP, TransactionDirection.OUT, "Testing");
        when(accountDAO.existsAccountWithAccountId(transactionRequest.accountId())).thenReturn(false);
        assertThatThrownBy(() -> transactionService.createTransaction(transactionRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with id [%s] not found".formatted(transactionRequest.accountId()));
    }

    @Test
    void createTransactionWithInvalidCurrency() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, "500", CustomCurrency.USD, TransactionDirection.IN, "Order-444");
        when(accountDAO.existsAccountWithAccountId(transactionRequest.accountId())).thenReturn(true);

        Balance sekBalance = new Balance(1L, transactionRequest.accountId(), 500000L, "SEK");
        when(accountDAO.getBalances(transactionRequest.accountId())).thenReturn(List.of(sekBalance));

        assertThatThrownBy(() -> transactionService.createTransaction(transactionRequest))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessage("Account [%s] does not support transactions in %s".formatted(transactionRequest.accountId(), transactionRequest.currency()));
    }

    @Test
    void accountDoesNotHaveEnoughFunds() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, "5000", CustomCurrency.USD, TransactionDirection.OUT, "Order-444");
        when(accountDAO.existsAccountWithAccountId(transactionRequest.accountId())).thenReturn(true);

        Balance usdBalance = new Balance(1L, transactionRequest.accountId(), 500L, "USD");
        when(accountDAO.getBalances(transactionRequest.accountId())).thenReturn(List.of(usdBalance));

        assertThatThrownBy(() -> transactionService.createTransaction(transactionRequest))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Balance [%s] does not have enough funds".formatted(usdBalance.getId()));
    }

    private TransactionRequest createTransactionRequest(Long accountId, String amount, CustomCurrency currency, TransactionDirection direction, String description) {
        return new TransactionRequest(accountId, amount, currency, direction, description);
    }
}
