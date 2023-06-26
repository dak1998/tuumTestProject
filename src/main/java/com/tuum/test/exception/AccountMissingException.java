package com.tuum.test.exception;

public class AccountMissingException extends RuntimeException{

    public AccountMissingException(String message) {
        super(message);
    }

}
