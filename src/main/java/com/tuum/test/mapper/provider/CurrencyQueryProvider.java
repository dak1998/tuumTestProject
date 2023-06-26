package com.tuum.test.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

public class CurrencyQueryProvider {

    public static String insertCurrency(String id, Long accountId,  String currency, Double amount) {
        return new SQL(){{
                INSERT_INTO("currency_account_mapping");
                VALUES("id, account_id, currency, available_amount", "#{id}, #{accountId},#{currency},#{amount}");
        }}.toString();
    }
}
