package com.tuum.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private String id;

    private String currency;

    private Long accountId;

    private Double availableAmount;

    private Timestamp createTimestamp;

    private Timestamp updateTimestamp;

}
