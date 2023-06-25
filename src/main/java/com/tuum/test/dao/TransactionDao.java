package com.tuum.test.dao;

import com.tuum.test.dto.*;
import com.tuum.test.exception.AccountNotFoundException;
import com.tuum.test.exception.TuumTestException;
import com.tuum.test.mapper.TransactionMapper;
import com.tuum.test.model.Transaction;
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
public class TransactionDao {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public TransactionWithBalanceResponseDTO insertTransactionInDB(TransactionCreationRequestDTO transactionCreationRequestDTO) throws TuumTestException {
        final String idForTransactionRecord = UUID.randomUUID().toString();
        final String transactionId = UUID.randomUUID().toString();

        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TransactionMapper transactionMapper = sqlSession.getMapper(TransactionMapper.class);
            transactionMapper.insertTransaction(idForTransactionRecord, transactionId, transactionCreationRequestDTO.getAccountId(),
                    transactionCreationRequestDTO.getAmount(), transactionCreationRequestDTO.getCurrency(), transactionCreationRequestDTO.getDirectionOfTransaction(), transactionCreationRequestDTO.getDescription());

            TransactionWithBalanceResponseDTO transactionWithBalanceResponseDTO = new TransactionWithBalanceResponseDTO();
            transactionWithBalanceResponseDTO.setAccountId(transactionCreationRequestDTO.getAccountId());
            transactionWithBalanceResponseDTO.setTransactionId(transactionId);
            transactionWithBalanceResponseDTO.setAmount(transactionCreationRequestDTO.getAmount());
            transactionWithBalanceResponseDTO.setCurrency(transactionCreationRequestDTO.getCurrency());
            transactionWithBalanceResponseDTO.setDirectionOfTransaction(transactionCreationRequestDTO.getDirectionOfTransaction());
            transactionWithBalanceResponseDTO.setDescription(transactionCreationRequestDTO.getDescription());

            return transactionWithBalanceResponseDTO;

        } catch (Exception e) {
            log.error("Error while inserting Transaction data in DB: ", e);
            throw new TuumTestException("Error while inserting Transaction data in DB: " + e.getMessage());
        }
    }

    public List<TransactionDetailsResponseDto> getTransactionsFromDB(Long accountId) throws AccountNotFoundException, TuumTestException {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TransactionMapper transactionMapper = sqlSession.getMapper(TransactionMapper.class);

            List<Transaction> transactionList = transactionMapper.getTransactionsByAccountId(accountId);

            return transactionList.stream()
                    .map(transaction -> TransactionDetailsResponseDto.builder()
                            .accountId(transaction.getAccountId())
                            .transactionId(transaction.getTransactionId())
                            .amount(transaction.getAmount())
                            .currency(transaction.getCurrency())
                            .directionOfTransaction(transaction.getDirection())
                            .description(transaction.getDescription())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error while getting transaction from DB: ", e);
            throw new TuumTestException("Error while getting transaction from DB: " + e.getMessage());

        }
    }
}
