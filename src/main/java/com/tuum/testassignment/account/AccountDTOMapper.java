package com.tuum.testassignment.account;

import com.tuum.testassignment.account.dto.AccountRequest;
import com.tuum.testassignment.account.dto.AccountResponse;
import com.tuum.testassignment.balance.Balance;
import com.tuum.testassignment.balance.BalanceResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountDTOMapper {

    Account accountRequestToAccount(AccountRequest request);

    @Mapping(source = "id", target = "accountId")
    AccountResponse accountToAccountResponse(Account account);

    BalanceResponse balanceToBalanceResponse(Balance balance);
}
