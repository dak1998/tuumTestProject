package com.tuum.test.service;

import com.tuum.test.dao.TransactionDao;
import com.tuum.test.dto.*;
import com.tuum.test.enums.TransactionDirectionEnum;
import com.tuum.test.exception.*;
import com.tuum.test.producer.MQProducer;
import com.tuum.test.util.ExceptionMessageConstants;
import com.tuum.test.util.RabbitMQConfigConstants;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    MQProducer producer;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    TransactionDao transactionDao;

    @Override
    public TransactionWithBalanceResponseDTO createTransaction(TransactionCreationRequestDTO transactionCreationRequestDTO) {

        performTransactionDataSanityChecks(transactionCreationRequestDTO);
        performTransactionRelatedChecksOnAccount(transactionCreationRequestDTO);

        return producer.publishForTransactionCreation(RabbitMQConfigConstants.ACCOUNT_BALANCE_UPDATE_ROUTING_KEY, transactionCreationRequestDTO);

    }

    @Override
    public List<TransactionDetailsResponseDto> getTransactions(Long accountId) {
        return transactionDao.getTransactionsFromDB(accountId);
    }

    private void performTransactionDataSanityChecks(TransactionCreationRequestDTO transactionCreationRequestDTO) {
        if(Objects.isNull(transactionCreationRequestDTO.getAccountId()) || transactionCreationRequestDTO.getAccountId()==0)
            throw new AccountMissingException(ExceptionMessageConstants.ACCOUNT_MISSING);
        if(StringUtils.isBlank(transactionCreationRequestDTO.getDescription()))
            throw new DescriptionMissingException(ExceptionMessageConstants.DESCRIPTION_MISSING);
        Double transactionAmount = transactionCreationRequestDTO.getAmount();
        if(transactionAmount.isNaN() || transactionAmount <= 0 || transactionAmount.isInfinite())
            throw new InvalidAmountException(ExceptionMessageConstants.INVALID_AMOUNT);
        if (!EnumUtils.isValidEnumIgnoreCase(TransactionDirectionEnum.class, transactionCreationRequestDTO.getDirectionOfTransaction()))
            throw new InvalidDirectionException(ExceptionMessageConstants.INVALID_DIRECTION);
    }

    private void performTransactionRelatedChecksOnAccount(TransactionCreationRequestDTO transactionCreationRequestDTO) {
        AccountDetailsResponseDTO accountForTransaction = accountService.getAccount(transactionCreationRequestDTO.getAccountId());
        boolean isCurrencyValidForAccount = accountForTransaction.getListOfBalances()
                .stream()
                .map(AccountCurrencyBalanceDTO::getCurrency)
                .toList()
                .contains(transactionCreationRequestDTO.getCurrency());
        if(!isCurrencyValidForAccount)
            throw new InvalidCurrencyException(String.format(ExceptionMessageConstants.INVALID_CURRENCY_FOR_ACCOUNT, transactionCreationRequestDTO.getCurrency(), transactionCreationRequestDTO.getAccountId()));

        if(transactionCreationRequestDTO.getDirectionOfTransaction().equalsIgnoreCase(TransactionDirectionEnum.OUT.getDirection())) {
            boolean hasSufficientFunds = accountForTransaction.getListOfBalances()
                    .stream()
                    .filter(balance -> balance.getCurrency().equalsIgnoreCase(transactionCreationRequestDTO.getCurrency()))
                    .toList()
                    .get(0)
                    .getAvailableAmount() >= transactionCreationRequestDTO.getAmount();

            if (!hasSufficientFunds)
                throw new InsufficientFundsException(String.format(ExceptionMessageConstants.INSUFFICIENT_FUNDS, transactionCreationRequestDTO.getAccountId(), transactionCreationRequestDTO.getAmount().toString()));
        }
    }

}
