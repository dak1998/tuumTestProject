package com.tuum.test.dao;

import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.dto.AccountCurrencyBalanceDTO;
import com.tuum.test.exception.AccountNotFoundException;
import com.tuum.test.exception.TuumTestException;
import com.tuum.test.mapper.AccountMapper;
import com.tuum.test.mapper.CurrencyMapper;
import com.tuum.test.model.Account;
import com.tuum.test.model.Currency;
import com.tuum.test.util.ExceptionMessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public AccountDetailsResponseDTO insertAccountInDB(AccountCreationRequestDTO accountCreationRequestDTO) throws TuumTestException {
        final String IdForAccountRecord = UUID.randomUUID().toString();

        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
            CurrencyMapper currencyMapper = sqlSession.getMapper(CurrencyMapper.class);
            accountMapper.insertAccount(IdForAccountRecord, accountCreationRequestDTO.getCustomerId(), accountCreationRequestDTO.getCountry());

            Account insertedAccount = accountMapper.getAccountById(IdForAccountRecord);

            for(String currency : accountCreationRequestDTO.getCurrencies()) {
                currencyMapper.insertCurrency(UUID.randomUUID().toString(), insertedAccount.getAccountId(), currency.toUpperCase(), 0.0);
            }

            List<Currency> currencyListForAccount = currencyMapper.getCurrencyListByAccountId(insertedAccount.getAccountId());

            return AccountDetailsResponseDTO.builder()
                    .accountId(insertedAccount.getAccountId())
                    .customerId(insertedAccount.getCustomerId())
                    .listOfBalances(currencyListForAccount
                            .stream()
                            .map(currency -> AccountCurrencyBalanceDTO.builder()
                                    .currency(currency.getCurrency())
                                    .availableAmount(currency.getAvailableAmount())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();



        } catch (Exception e) {
            log.error("Error while inserting Account data in DB: ", e);
            throw new TuumTestException("Error while inserting Account data in DB: " + e.getMessage());
        }
    }

    public AccountDetailsResponseDTO getAccountFromDB(Long accountId) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
            CurrencyMapper currencyMapper = sqlSession.getMapper(CurrencyMapper.class);

            Account account = accountMapper.getAccountByAccountId(accountId);

            if(account == null)
                throw new AccountNotFoundException(ExceptionMessageConstants.ACCOUNT_NOT_FOUND);

            List<Currency> currencyListForAccount = currencyMapper.getCurrencyListByAccountId(account.getAccountId());

            return AccountDetailsResponseDTO.builder()
                    .accountId(account.getAccountId())
                    .customerId(account.getCustomerId())
                    .listOfBalances(currencyListForAccount
                            .stream()
                            .map(currency -> AccountCurrencyBalanceDTO.builder()
                                    .currency(currency.getCurrency())
                                    .availableAmount(currency.getAvailableAmount())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

        } catch (Exception e) {
                log.error("Error while getting account from DB: ", e);
                throw new TuumTestException("Error while getting account from DB: " + e.getMessage());

        }
    }

    public void updateAccountBalanceForTransactionInDB(Long accountId, String currency, Double updatedBalance) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CurrencyMapper currencyMapper = sqlSession.getMapper(CurrencyMapper.class);

            currencyMapper.updateAccountBalance(accountId, currency, updatedBalance);
        }
    }

}
