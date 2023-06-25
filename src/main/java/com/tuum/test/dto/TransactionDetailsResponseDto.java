package com.tuum.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailsResponseDto {

    private Long accountId;

    private String transactionId;

    private Double amount;

    private String currency;

    private String directionOfTransaction;

    private String description;
}
