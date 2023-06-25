package com.tuum.test.service;

import com.tuum.test.dto.*;

import java.util.List;

public interface ITransactionService {

    TransactionWithBalanceResponseDTO createTransaction(TransactionCreationRequestDTO transactionCreationRequestDTO);

    List<TransactionDetailsResponseDto> getTransactions(Long accountId);
}
