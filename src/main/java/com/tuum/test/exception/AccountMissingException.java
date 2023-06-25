package com.tuum.test.exception;

public class AccountMissingException extends RuntimeException{

    public AccountMissingException(String message) {
        super(message);
    }

    public AccountMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
