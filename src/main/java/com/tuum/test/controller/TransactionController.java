package com.tuum.test.controller;

import com.tuum.test.dto.*;
import com.tuum.test.exception.*;
import com.tuum.test.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    TransactionServiceImpl transactionService;

    @PostMapping("transaction")
    public ResponseEntity<TransactionWithBalanceResponseDTO> createTransaction(@RequestBody TransactionCreationRequestDTO transactionCreationRequestDTO)
            throws InvalidCurrencyException, InvalidDirectionException, InvalidAmountException, InsufficientFundsException, AccountMissingException, DescriptionMissingException, TuumTestException {
        TransactionWithBalanceResponseDTO transactionWithBalanceResponseDTO = transactionService.createTransaction(transactionCreationRequestDTO);
        return new ResponseEntity<>(transactionWithBalanceResponseDTO, HttpStatus.OK);
    }

    @GetMapping("transactions/{accountId}")
    public ResponseEntity<List<TransactionDetailsResponseDto>> getTransaction(@PathVariable Long accountId) throws AccountNotFoundException, TuumTestException {
        List<TransactionDetailsResponseDto> transactionDetailsResponseDto = transactionService.getTransactions(accountId);
        return new ResponseEntity<>(transactionDetailsResponseDto, HttpStatus.OK);
    }
}
