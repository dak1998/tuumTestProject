package com.tuum.test.consumer;

import com.tuum.test.dao.AccountDao;
import com.tuum.test.dao.TransactionDao;
import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.dto.TransactionCreationRequestDTO;
import com.tuum.test.dto.TransactionWithBalanceResponseDTO;
import com.tuum.test.enums.TransactionDirectionEnum;
import com.tuum.test.exception.TuumTestException;
import com.tuum.test.service.AccountServiceImpl;
import com.tuum.test.util.RabbitMQConfigConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQConsumer {

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    AccountDao accountDao;

    @RabbitListener(queues = RabbitMQConfigConstants.ACCOUNT_CREATION_QNAME)
    public AccountDetailsResponseDTO receiveAccountRequestDTO(AccountCreationRequestDTO accountCreationRequestDTO) throws TuumTestException {
        return accountDao.insertAccountInDB(accountCreationRequestDTO);
    }


    @RabbitListener(queues = RabbitMQConfigConstants.ACCOUNT_BALANCE_UPDATE_QNAME)
    public TransactionWithBalanceResponseDTO receiveTransactionCreationRequestDTO(TransactionCreationRequestDTO transactionCreationRequestDTO) {
        Double updatedBalance = accountService.updateBalanceForTransaction(transactionCreationRequestDTO.getAccountId(), transactionCreationRequestDTO.getCurrency(), transactionCreationRequestDTO.getAmount(), TransactionDirectionEnum.valueOf(transactionCreationRequestDTO.getDirectionOfTransaction().toUpperCase()));

        TransactionWithBalanceResponseDTO transactionWithBalanceResponseDTO = transactionDao.insertTransactionInDB(transactionCreationRequestDTO);

        transactionWithBalanceResponseDTO.setBalanceAfterTransaction(updatedBalance);

        return transactionWithBalanceResponseDTO;
    }
}
