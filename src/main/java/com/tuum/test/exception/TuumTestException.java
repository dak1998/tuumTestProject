package com.tuum.test.exception;

public class TuumTestException extends RuntimeException {

    public TuumTestException(String message) {
        super(message);
    }

    public TuumTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
