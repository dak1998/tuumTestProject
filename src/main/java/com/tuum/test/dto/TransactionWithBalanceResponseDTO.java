package com.tuum.test.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionWithBalanceResponseDTO extends TransactionDetailsResponseDto {

    private Double balanceAfterTransaction;
}
