package com.tuum.testassignment.account.dto;

import com.tuum.testassignment.balance.BalanceResponse;

import java.util.List;

public record AccountResponse(Long accountId,
                              Long customerId,
                              List<BalanceResponse> balances) {

}

