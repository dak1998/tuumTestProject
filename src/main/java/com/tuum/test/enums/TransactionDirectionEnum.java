package com.tuum.test.enums;

public enum TransactionDirectionEnum {
    IN("IN"),
    OUT("OUT");

    private final String direction;
    TransactionDirectionEnum(String direction) {
        this.direction = direction;
    }

    public String getDirection() {return direction;}
}
