package com.tuum.testassignment.transaction;

import com.tuum.testassignment.PostgreSQLExtension;
import com.tuum.testassignment.account.Account;
import com.tuum.testassignment.account.AccountDAO;
import com.tuum.testassignment.balance.Balance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PostgreSQLExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
class TransactionDataAccessTest {

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Test
    void createTransaction() {
        // Init account
        long accountId = 1L;
        Account account = new Account(
                accountId,
                2L,
                "EE",
                List.of(
                        new Balance(accountId, 10000L, "EUR")
                )
        );
        accountDAO.createAccount(account);

        Transaction expectedTransaction = new Transaction(accountId, 100L, "EUR", "OUT", "Out transaction");

        transactionDAO.createTransaction(expectedTransaction);
        assertThat(expectedTransaction.getId()).isNotNull();

        List<Transaction> actualTransactions = transactionDAO.getTransactionsByAccountId(accountId);
        assertThat(actualTransactions)
                .isNotEmpty()
                .hasSize(1)
                .satisfiesExactly(transaction -> {
                    assertThat(transaction.getAccountId()).isEqualTo(accountId);
                    assertThat(transaction.getAmount()).isEqualTo(expectedTransaction.getAmount());
                    assertThat(transaction.getCurrency()).isEqualTo(expectedTransaction.getCurrency());
                    assertThat(transaction.getDirection()).isEqualTo(expectedTransaction.getDirection());
                    assertThat(transaction.getDescription()).isEqualTo(expectedTransaction.getDescription());
                });

    }
}
