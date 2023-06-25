package com.tuum.test.exception;

public class InvalidDirectionException extends RuntimeException{

    public InvalidDirectionException(String message) {
        super(message);
    }

    public InvalidDirectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
