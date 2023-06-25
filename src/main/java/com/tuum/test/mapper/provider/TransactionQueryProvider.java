package com.tuum.test.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class TransactionQueryProvider {

    public static String insertTransaction(String id, String transactionId, Long accountId,
                                            Double amount, String currency, String direction, String description) {
        return new SQL(){{
            INSERT_INTO("transaction");
            VALUES("id, transaction_id, account_id, amount, currency, direction, description", "#{id}, #{transactionId}, #{accountId}, #{amount}, #{currency}, #{direction}, #{description}");
        }}.toString();
    }
}
