package com.tuum.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    String id;
    String transactionId;
    Long accountId;
    Double amount;
    String currency;
    String direction;
    String description;

}
