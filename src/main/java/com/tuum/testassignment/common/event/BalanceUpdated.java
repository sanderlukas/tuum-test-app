package com.tuum.testassignment.common.event;

public record BalanceUpdated(Long id,
                             Long accountId,
                             String currency,
                             Long newAmount) {

}
