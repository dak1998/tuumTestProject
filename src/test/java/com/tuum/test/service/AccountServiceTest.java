package com.tuum.test.service;

import com.tuum.test.dao.AccountDao;
import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountCurrencyBalanceDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.enums.TransactionDirectionEnum;
import com.tuum.test.exception.InvalidCurrencyException;
import com.tuum.test.producer.MQProducer;
import com.tuum.test.util.ExceptionMessageConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    MQProducer mockProducer;

    @Mock
    AccountDao mockAccountDao;

    @InjectMocks
    AccountServiceImpl accountServiceUnderTest;

    @Test
    public void testCreateAccountThrowsInvalidCurrencyException() throws InvalidCurrencyException {
        AccountCreationRequestDTO accountCreationRequestDTO = mock(AccountCreationRequestDTO.class);
        when(accountCreationRequestDTO.getCurrencies()).thenReturn(Collections.singletonList("JPY"));


        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class, () -> accountServiceUnderTest.createAccount(accountCreationRequestDTO));

        String expectedMessage = ExceptionMessageConstants.INVALID_CURRENCY;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateAccount() {
        AccountCreationRequestDTO accountCreationRequestDTO = mock(AccountCreationRequestDTO.class);
        when(accountCreationRequestDTO.getCurrencies()).thenReturn(Collections.singletonList("EUR"));

        accountServiceUnderTest.createAccount(accountCreationRequestDTO);

        Mockito.verify(mockProducer, atLeastOnce()).publishForAccountCreation(anyString(), any(AccountCreationRequestDTO.class));

    }

    @Test
    public void testGetAccount() {
        accountServiceUnderTest.getAccount(anyLong());
        Mockito.verify(mockAccountDao, atLeastOnce()).getAccountFromDB(anyLong());
    }

    @Test
    public void testUpdateBalanceForTransactionForInBoundTransaction() {
        Long accountId = 10001L;
        String currency = "EUR";
        Double amount = 500d;
        TransactionDirectionEnum transactionDirection = TransactionDirectionEnum.IN;

        AccountDetailsResponseDTO accountDetailsResponseDTO = mock(AccountDetailsResponseDTO.class);
        List<AccountCurrencyBalanceDTO> accountCurrencyBalanceDTOList = new ArrayList<>();
        accountCurrencyBalanceDTOList.add(new AccountCurrencyBalanceDTO(500d, "EUR"));
        when(accountDetailsResponseDTO.getListOfBalances()).thenReturn(accountCurrencyBalanceDTOList);
        when(mockAccountDao.getAccountFromDB(anyLong())).thenReturn(accountDetailsResponseDTO);

        Double expectedUpdatedAmount = 1000d;
        Double actualUpdatedAmount = accountServiceUnderTest.updateBalanceForTransaction(accountId, currency, amount,transactionDirection);

        assertEquals(expectedUpdatedAmount, actualUpdatedAmount);
    }

    @Test
    public void testUpdateBalanceForTransactionForOutBoundTransaction() {
        Long accountId = 10001L;
        String currency = "EUR";
        Double amount = 300d;
        TransactionDirectionEnum transactionDirection = TransactionDirectionEnum.OUT;

        AccountDetailsResponseDTO accountDetailsResponseDTO = mock(AccountDetailsResponseDTO.class);
        List<AccountCurrencyBalanceDTO> accountCurrencyBalanceDTOList = new ArrayList<>();
        accountCurrencyBalanceDTOList.add(new AccountCurrencyBalanceDTO(500d, "EUR"));
        when(accountDetailsResponseDTO.getListOfBalances()).thenReturn(accountCurrencyBalanceDTOList);
        when(mockAccountDao.getAccountFromDB(anyLong())).thenReturn(accountDetailsResponseDTO);

        Double expectedUpdatedAmount = 200d;
        Double actualUpdatedAmount = accountServiceUnderTest.updateBalanceForTransaction(accountId, currency, amount,transactionDirection);

        assertEquals(expectedUpdatedAmount, actualUpdatedAmount);
    }

}
