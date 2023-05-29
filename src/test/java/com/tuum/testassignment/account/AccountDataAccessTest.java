package com.tuum.testassignment.account;

import com.tuum.testassignment.PostgreSQLExtension;
import com.tuum.testassignment.balance.Balance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PostgreSQLExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
@MybatisTest
class AccountDataAccessTest {

    @Autowired
    private AccountDAO accountDAO;

    @Test
    void selectAccountById() {
        long accountId = 1L;
        Account expectedAccount = new Account(
                accountId,
                2L,
                "EE",
                List.of(
                        new Balance(accountId, 10000L, "EUR")
                )
        );
        accountDAO.createAccount(expectedAccount);
        accountDAO.createBalances(expectedAccount.getBalances());

        Optional<Account> actualAccount = accountDAO.getAccountById(accountId);

        assertThat(actualAccount).isPresent().hasValueSatisfying(acc -> {
            assertThat(acc.getId()).isEqualTo(accountId);
            assertThat(acc.getCustomerId()).isEqualTo(expectedAccount.getCustomerId());
            assertThat(acc.getCountry()).isEqualTo(expectedAccount.getCountry());
            assertThat(acc.getBalances()).isNotEmpty();
            Balance actualBalance = acc.getBalances().stream().findFirst().get();
            Balance expectedBalance = expectedAccount.getBalances().get(0);
            assertThat(actualBalance.getAccountId()).isEqualTo(accountId);
            assertThat(actualBalance.getId()).isEqualTo(expectedBalance.getId());
            assertThat(actualBalance.getCurrency()).isEqualTo(expectedBalance.getCurrency());
            assertThat(actualBalance.getAmount()).isEqualTo(expectedBalance.getAmount());
        });
    }

    @Test
    void accountDoesNotExist() {
        Optional<Account> actualAccount = accountDAO.getAccountById(0L);
        assertThat(actualAccount).isEmpty();
    }

    @Test
    void updateAccountBalance() {
        Balance expectedBalance = new Balance(1L, 10000L, "EUR");
        Account expectedAccount = new Account(
                2L,
                "EE",
                List.of(
                        expectedBalance
                )
        );

        accountDAO.createAccount(expectedAccount);
        assertThat(expectedAccount.getId()).isNotNull();

        expectedBalance.setAccountId(expectedAccount.getId());
        accountDAO.createBalances(List.of(expectedBalance));
        List<Balance> actualBalances = accountDAO.getBalances(expectedAccount.getId());
        assertThat(actualBalances)
                .isNotEmpty()
                .satisfiesExactly(balance -> assertThat(balance.getId()).isNotNull());

        expectedBalance.setAmount(5000L);
        accountDAO.updateBalance(expectedBalance);

        actualBalances = accountDAO.getBalances(expectedAccount.getId());
        assertThat(actualBalances.get(0).getAmount()).isEqualTo(expectedBalance.getAmount());
    }

    @Test
    void accountExists() {
        long accountId = 1L;
        Account account = new Account(
                accountId,
                2L,
                "EE",
                List.of(
                        new Balance(1L, accountId, 10000L, "EUR")
                )
        );
        accountDAO.createAccount(account);

        boolean exists = accountDAO.existsAccountWithAccountId(account.getId());

        assertThat(exists).isTrue();
    }

}
