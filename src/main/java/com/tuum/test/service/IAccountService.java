package com.tuum.test.service;

import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.enums.TransactionDirectionEnum;

public interface IAccountService {

    AccountDetailsResponseDTO createAccount(AccountCreationRequestDTO accountCreationRequestDTO);
    AccountDetailsResponseDTO getAccount(Long accountId);
    Double updateBalanceForTransaction(Long accountId, String currency, Double amount, TransactionDirectionEnum transactionDirection);
}
