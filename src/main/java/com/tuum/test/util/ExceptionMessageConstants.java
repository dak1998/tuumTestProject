package com.tuum.test.util;

public class ExceptionMessageConstants {
    public static final String INVALID_CURRENCY = "ERROR: Invalid currency input. Allowed values are EUR, SEK, GBP, USD";
    public static final String INVALID_CURRENCY_FOR_ACCOUNT = "ERROR: Invalid currency for transaction. Provided currency %s doesn't exist for account %s";
    public static final String ACCOUNT_NOT_FOUND = "ERROR: Account ID given in input is not valid. Account with this Account ID does not exist";
    public static final String ACCOUNT_MISSING = "ERROR: Account ID for transaction not provided or is blank in input";
    public static final String DESCRIPTION_MISSING = "ERROR: Prove Description for Transaction. Description for transaction not provided or is blank in input";
    public static final String INVALID_AMOUNT = "ERROR: Kindly input a positive non zero amount for transaction";
    public static final String INVALID_DIRECTION = "ERROR: Transaction direction must be \"IN\" or \"OUT\"";
    public static final String INSUFFICIENT_FUNDS = "ERROR: Account %s has insufficient funds for outbound transaction of %s";
}
