package com.tuum.test.controller;

import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.exception.AccountNotFoundException;
import com.tuum.test.exception.InvalidCurrencyException;
import com.tuum.test.exception.TuumTestException;
import com.tuum.test.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    AccountServiceImpl accountService;

    @PostMapping("account")
    public ResponseEntity<AccountDetailsResponseDTO> createAccount(@RequestBody AccountCreationRequestDTO accountCreationRequestDTO) throws InvalidCurrencyException, TuumTestException {
        AccountDetailsResponseDTO accountDetailsResponseDTO = accountService.createAccount(accountCreationRequestDTO);
          return new ResponseEntity<>(accountDetailsResponseDTO, HttpStatus.OK);
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<AccountDetailsResponseDTO> getAccount(@PathVariable Long accountId) throws AccountNotFoundException, TuumTestException {
        AccountDetailsResponseDTO accountDetailsResponseDTO = accountService.getAccount(accountId);
        return new ResponseEntity<>(accountDetailsResponseDTO, HttpStatus.OK);
    }

}
