package com.tuum.testassignment.exception;

import com.tuum.testassignment.common.CustomCurrency;

public class InvalidCurrencyException extends RuntimeException {

    public InvalidCurrencyException(String exception, Long accountId, CustomCurrency currency) {
        super(String.format(exception, accountId, currency));
    }
}
