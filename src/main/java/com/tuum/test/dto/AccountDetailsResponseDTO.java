package com.tuum.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailsResponseDTO {

    private Long accountId;

    private String customerId;

    private List<AccountCurrencyBalanceDTO> listOfBalances;

}
