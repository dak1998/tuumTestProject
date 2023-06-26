package com.tuum.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String id;
    private String customerId;
    private Long accountId;
    private String country;
    private Timestamp createTimestamp;
    private Timestamp updateTimestamp;
}
