package com.tuum.testassignment.account;

import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;
import com.tuum.testassignment.balance.Balance;
import com.tuum.testassignment.exception.ResourceNotFoundException;
import com.tuum.testassignment.rabbitmq.RabbitMQMessageProducer;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountDAO accountDao;
    private final AccountDTOMapper accountMapper;
    private final RabbitMQMessageProducer producer;

    public AccountService(AccountDAO accountDao, AccountDTOMapper accountDTOMapper, RabbitMQMessageProducer producer) {
        this.accountDao = accountDao;
        this.accountMapper = accountDTOMapper;
        this.producer = producer;
    }

    public AccountResponse createAccount(AccountRequest request) {
        Account account = accountMapper.accountRequestToAccount(request);
        accountDao.createAccount(account);

        account.setBalances(
                request.currencies().stream()
                        .map(currency -> new Balance(account.getId(), 0L, currency.name()))
                        .toList()
        );

        accountDao.createBalances(account.getBalances());
        AccountResponse accountResponse = accountMapper.accountToAccountResponse(account);
        producer.publish(account);
        return accountResponse;
    }

    public AccountResponse getAccount(Long accountId) {
        return accountDao.getAccountById(accountId)
                .map(accountMapper::accountToAccountResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id [%s] not found", accountId));
    }
}
