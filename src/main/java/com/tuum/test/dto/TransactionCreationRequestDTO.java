package com.tuum.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreationRequestDTO {

    private Long accountId;

    private Double amount;

    private String currency;

    private String directionOfTransaction;

    private String description;
}
