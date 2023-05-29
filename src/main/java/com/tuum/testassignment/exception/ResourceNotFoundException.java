package com.tuum.testassignment.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message, Long accountId) {
        super(String.format(message, accountId));
    }
}
