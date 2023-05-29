package com.tuum.testassignment.account;

import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;
import com.tuum.testassignment.balance.Balance;
import com.tuum.testassignment.common.Country;
import com.tuum.testassignment.common.CustomCurrency;
import com.tuum.testassignment.exception.ResourceNotFoundException;
import com.tuum.testassignment.rabbitmq.RabbitMQMessageProducer;

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
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {
        AccountDTOMapperImpl.class
})
class AccountServiceTest {

    @Autowired
    private AccountDTOMapper accountDTOMapper;

    @Mock
    private RabbitMQMessageProducer messageProducer;

    @Mock
    private AccountDAO accountDAO;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountDAO, accountDTOMapper, messageProducer);
    }

    @Test
    void canGetPersistedAccount() {
        Long accountId = 1L;

        Account account = new Account(
                accountId,
                2L,
                "EE",
                List.of(
                        new Balance(1L, accountId, 10000L, "EUR")
                )
        );

        when(accountDAO.getAccountById(account.getId())).thenReturn(Optional.of(account));

        AccountResponse expectedResponse = accountDTOMapper.accountToAccountResponse(account);
        AccountResponse actualResponse = accountService.getAccount(accountId);
        assertThat(expectedResponse).isEqualTo(actualResponse);
    }

    @Test
    void willThrowExceptionWhenAccountNotFound() {
        Long accountId = 2L;
        when(accountDAO.getAccountById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(accountId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with id [%s] not found".formatted(accountId));
    }

    @Test
    void createNewAccount() {
        AccountRequest request = new AccountRequest(1L, Country.EE, Set.of(CustomCurrency.EUR, CustomCurrency.SEK));
        accountService.createAccount(request);

        ArgumentCaptor<Account> customerArgumentCaptor = ArgumentCaptor.forClass(Account.class);

        verify(accountDAO).createAccount(customerArgumentCaptor.capture());

        Account capturedAccount = customerArgumentCaptor.getValue();

        assertThat(capturedAccount.getId()).isNull();
        assertThat(capturedAccount.getCustomerId()).isEqualTo(request.customerId());
        assertThat(capturedAccount.getCountry()).isEqualTo(request.country().name());
    }
}
