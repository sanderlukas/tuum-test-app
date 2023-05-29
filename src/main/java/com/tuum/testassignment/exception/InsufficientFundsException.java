package com.tuum.testassignment.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message, Long balanceId) {
        super(String.format(message, balanceId));
    }
}
