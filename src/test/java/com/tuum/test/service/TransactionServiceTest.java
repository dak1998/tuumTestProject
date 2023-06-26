package com.tuum.test.service;

import com.tuum.test.dao.TransactionDao;
import com.tuum.test.dto.AccountCurrencyBalanceDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.dto.TransactionCreationRequestDTO;
import com.tuum.test.exception.*;
import com.tuum.test.producer.MQProducer;
import com.tuum.test.util.ExceptionMessageConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    MQProducer mockProducer;

    @Mock
    AccountServiceImpl mockAccountService;

    @Mock
    TransactionDao mockTransactionDao;

    @InjectMocks
    TransactionServiceImpl transactionServiceUnderTest;


    @Test
    public void testCreateTransactionThrowsAccountMissingException() throws AccountMissingException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(null);

        AccountMissingException exception = assertThrows(AccountMissingException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = ExceptionMessageConstants.ACCOUNT_MISSING;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransactionThrowsDescriptionMissingException() throws DescriptionMissingException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("");

        DescriptionMissingException exception = assertThrows(DescriptionMissingException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = ExceptionMessageConstants.DESCRIPTION_MISSING;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransactionThrowsInvalidAmountException() throws InvalidAmountException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("payment1");
        when(transactionCreationRequestDTO.getAmount()).thenReturn(-1d);

        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = ExceptionMessageConstants.INVALID_AMOUNT;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransactionThrowsInvalidDirectionException() throws InvalidDirectionException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("payment1");
        when(transactionCreationRequestDTO.getAmount()).thenReturn(500d);
        when(transactionCreationRequestDTO.getDirectionOfTransaction()).thenReturn("debit");

        InvalidDirectionException exception = assertThrows(InvalidDirectionException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = ExceptionMessageConstants.INVALID_DIRECTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransactionThrowsInvalidCurrencyException() throws InvalidCurrencyException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("payment1");
        when(transactionCreationRequestDTO.getAmount()).thenReturn(500d);
        when(transactionCreationRequestDTO.getDirectionOfTransaction()).thenReturn("IN");
        when(transactionCreationRequestDTO.getCurrency()).thenReturn("GBP");

        AccountDetailsResponseDTO accountDetailsResponseDTO = mock(AccountDetailsResponseDTO.class);
        List<AccountCurrencyBalanceDTO> accountCurrencyBalanceDTOList = new ArrayList<>();
        accountCurrencyBalanceDTOList.add(new AccountCurrencyBalanceDTO(500d, "EUR"));
        when(accountDetailsResponseDTO.getListOfBalances()).thenReturn(accountCurrencyBalanceDTOList);
        when(mockAccountService.getAccount(anyLong())).thenReturn(accountDetailsResponseDTO);


        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = "ERROR: Invalid currency for transaction";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransactionThrowsInsufficientFundsException() throws InsufficientFundsException {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("payment1");
        when(transactionCreationRequestDTO.getAmount()).thenReturn(500d);
        when(transactionCreationRequestDTO.getDirectionOfTransaction()).thenReturn("OUT");
        when(transactionCreationRequestDTO.getCurrency()).thenReturn("EUR");

        AccountDetailsResponseDTO accountDetailsResponseDTO = mock(AccountDetailsResponseDTO.class);
        List<AccountCurrencyBalanceDTO> accountCurrencyBalanceDTOList = new ArrayList<>();
        accountCurrencyBalanceDTOList.add(new AccountCurrencyBalanceDTO(100d, "EUR"));
        when(accountDetailsResponseDTO.getListOfBalances()).thenReturn(accountCurrencyBalanceDTOList);
        when(mockAccountService.getAccount(anyLong())).thenReturn(accountDetailsResponseDTO);


        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO));

        String expectedMessage = "has insufficient funds for outbound transaction";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateTransaction() {
        TransactionCreationRequestDTO transactionCreationRequestDTO = mock(TransactionCreationRequestDTO.class);
        when(transactionCreationRequestDTO.getAccountId()).thenReturn(10001L);
        when(transactionCreationRequestDTO.getDescription()).thenReturn("payment1");
        when(transactionCreationRequestDTO.getAmount()).thenReturn(100d);
        when(transactionCreationRequestDTO.getDirectionOfTransaction()).thenReturn("OUT");
        when(transactionCreationRequestDTO.getCurrency()).thenReturn("EUR");

        AccountDetailsResponseDTO accountDetailsResponseDTO = mock(AccountDetailsResponseDTO.class);
        List<AccountCurrencyBalanceDTO> accountCurrencyBalanceDTOList = new ArrayList<>();
        accountCurrencyBalanceDTOList.add(new AccountCurrencyBalanceDTO(500d, "EUR"));
        when(accountDetailsResponseDTO.getListOfBalances()).thenReturn(accountCurrencyBalanceDTOList);
        when(mockAccountService.getAccount(anyLong())).thenReturn(accountDetailsResponseDTO);

        transactionServiceUnderTest.createTransaction(transactionCreationRequestDTO);

        Mockito.verify(mockProducer, atLeastOnce()).publishForTransactionCreation(anyString(), any(TransactionCreationRequestDTO.class));

    }

    @Test
    public void testGetTransactions() {
        transactionServiceUnderTest.getTransactions(anyLong());
        Mockito.verify(mockTransactionDao, atLeastOnce()).getTransactionsFromDB(anyLong());
    }
}
