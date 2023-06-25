package com.tuum.test.enums;

public enum CurrencyEnum {
    EUR("EUR"),
    SEK("SEK"),
    GBP("GBP"),
    USD("USD");

    private final String type;
    CurrencyEnum(String type) {
        this.type = type;
    }

    public String getType() {return type;}
}
