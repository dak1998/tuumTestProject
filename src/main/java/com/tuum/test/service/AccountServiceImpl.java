package com.tuum.test.service;

import com.tuum.test.dao.AccountDao;
import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.enums.CurrencyEnum;
import com.tuum.test.enums.TransactionDirectionEnum;
import com.tuum.test.exception.InvalidCurrencyException;
import com.tuum.test.producer.MQProducer;
import com.tuum.test.util.ExceptionMessageConstants;
import com.tuum.test.util.RabbitMQConfigConstants;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountServiceImpl implements IAccountService{

    @Autowired
    MQProducer producer;

    @Autowired
    AccountDao accountDao;

    @Override
    public AccountDetailsResponseDTO createAccount(AccountCreationRequestDTO accountCreationRequestDTO) {

        for(String currency : accountCreationRequestDTO.getCurrencies())
            if (!EnumUtils.isValidEnumIgnoreCase(CurrencyEnum.class, currency))
                throw new InvalidCurrencyException(ExceptionMessageConstants.INVALID_CURRENCY);

        return producer.publishForAccountCreation(RabbitMQConfigConstants.ACCOUNT_CREATION_ROUTING_KEY, accountCreationRequestDTO);

    }

    @Override
    public AccountDetailsResponseDTO getAccount(Long accountId) {
        return accountDao.getAccountFromDB(accountId);
    }

    @Override
    public Double updateBalanceForTransaction(Long accountId, String currency, Double amount, TransactionDirectionEnum transactionDirection) {

        Double currentAccountBalance =  getAccount(accountId).getListOfBalances()
                .stream()
                .filter(balance -> balance.getCurrency().equalsIgnoreCase(currency))
                .toList()
                .get(0)
                .getAvailableAmount();

        Double updatedBalance = currentAccountBalance;

        if(transactionDirection.equals(TransactionDirectionEnum.IN)) {
            updatedBalance = currentAccountBalance + amount;
        } else if(transactionDirection.equals(TransactionDirectionEnum.OUT)) {
            updatedBalance = currentAccountBalance - amount;
        }

        accountDao.updateAccountBalanceForTransactionInDB(accountId, currency, updatedBalance);

        return updatedBalance;
    }
}
