package com.tuum.test.producer;

import com.tuum.test.dto.AccountCreationRequestDTO;
import com.tuum.test.dto.AccountDetailsResponseDTO;
import com.tuum.test.dto.TransactionCreationRequestDTO;
import com.tuum.test.dto.TransactionWithBalanceResponseDTO;
import com.tuum.test.exception.TuumTestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    DirectExchange directExchange;

    public AccountDetailsResponseDTO publishForAccountCreation(String routingKey, AccountCreationRequestDTO message) throws TuumTestException {
        try {
            AccountDetailsResponseDTO accountDetailsResponseDTO = rabbitTemplate.convertSendAndReceiveAsType(
                    directExchange.getName(),
                    routingKey,
                    message,
                    new ParameterizedTypeReference<>() {
                    });

            return accountDetailsResponseDTO;
        } catch (Exception e) {
            log.error("Error occurred while publishing/consuming message from MQ: ", e);
            throw new TuumTestException("Error occurred while publishing/consuming message from MQ: " + e.getMessage());
        }
    }

    public TransactionWithBalanceResponseDTO publishForTransactionCreation(String routingKey, TransactionCreationRequestDTO message) throws TuumTestException {
        try {
            TransactionWithBalanceResponseDTO transactionWithBalanceResponseDTO = rabbitTemplate.convertSendAndReceiveAsType(
                    directExchange.getName(),
                    routingKey,
                    message,
                    new ParameterizedTypeReference<>() {
                    });

            return transactionWithBalanceResponseDTO;
        } catch (Exception e) {
            log.error("Error occurred while publishing/consuming message from MQ: ", e);
            throw new TuumTestException("Error occurred while publishing/consuming message from MQ: " + e.getMessage());
        }
    }

}
